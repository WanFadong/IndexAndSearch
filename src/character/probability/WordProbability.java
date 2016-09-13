package character.probability;

import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import dao.ConnectionFactory;
import model.BasicWordInfoPO;
import model.WordInPoetryPO;
import model.WordInfoPO;

/**
 * 计算all_character中所有模式的可能性； 选取可能性前10的词语；
 * 
 * @author fadongwan
 *
 */

public class WordProbability {
	private final String invertedIndexTable = "inverted_index";
	private final String wordLengthKey = "word_length";
	private final String wordFrequencyKey = "word_frequency";
	private final String indexAvgKey = "index_avg";
	private final String titleKey = "title";
	private final String authorKey = "author";
	private final String verseKey = "verse";
	private final String wordInfoKey = "word_info";
	private final String poetryIdKey = "poetry_id";

	private final String colName = "all_character";
	private final String idKey = "_id";
	private final String setKey = "$set";
	private final String probabilityKey = "probability";
	private final String valueKey = "value";

	/**
	 * 计算
	 */
	public void calAllCharacterProbablity() {
		ConnectionFactory factory = new ConnectionFactory();
		MongoDatabase db = factory.getDBConnection();
		MongoCollection<Document> col = db.getCollection(colName);
		FindIterable<Document> result = col.find();
		for (Document doc : result) {
			String character = doc.getString(idKey);
			ProbabilityCalService cal = new ProbabilityCalculator();
			double probability = cal.calProbabilityByCharacter(character);
			// 更新数据库
			BasicDBObject filter = new BasicDBObject("_id", character);
			BasicDBObject data = new BasicDBObject(setKey, new BasicDBObject(probabilityKey, probability));
			col.updateOne(filter, data);
		}
		factory.closeConnection();
	}

	/**
	 * 选取可能性最大的k个词（只需要词本身即可）
	 * 
	 * @param k
	 * @return
	 */
	public WordInfoPO[] selectTopWords(final int k) {
		ConnectionFactory factory = new ConnectionFactory();
		MongoDatabase db = factory.getDBConnection();
		BasicDBObject sortBson = new BasicDBObject(probabilityKey, -1);
		int count = 0;
		int skip = 0;
		int limit = k;
		ArrayList<WordInfoPO> topWordList = new ArrayList<WordInfoPO>();
		while (count < k) {
			MongoCollection<Document> col = db.getCollection(colName);
			FindIterable<Document> result = col.find().sort(sortBson).limit(1).skip(skip);
			Document doc = result.iterator().next();
			// 从倒排表中查找数据
			String character = doc.getString(idKey);
			double probability = doc.getDouble(probabilityKey);
			// System.out.print(doc.get(probabilityKey) + "||");
			// System.out.println(character);

			int num = doc.getInteger(valueKey);// 具有相同character的条目数
			String[] tmp = character.split("#");
			int frequency = Integer.valueOf(tmp[0]);
			int length = Integer.valueOf(tmp[1]);
			int indexAvg = Integer.valueOf(tmp[2]);

			col = db.getCollection(invertedIndexTable);
			BasicDBObject filter = new BasicDBObject();
			filter.put(wordFrequencyKey, frequency);
			filter.put(wordLengthKey, length);
			filter.put(indexAvgKey, indexAvg);
			result = col.find(filter).limit(limit);// 有问题，不能limit
			// 从result中提取结果
			for (Document wordInfo : result) {
				WordInfoPO wordInfoPO = documentToWordInfo(wordInfo);
				wordInfoPO.setProbability(probability);
				// // 排除已知的典面
				// boolean isPattern = isPattern(wordInfoPO.getId());
				// if (!isPattern) {
				// topWordList.add(wordInfoPO);
				// }
				topWordList.add(wordInfoPO);
			}

			count = count + num;
			limit = limit - num;
			skip++;
			// System.out.println(count + "#" + limit);
		}
		factory.closeConnection();
		WordInfoPO[] topWords = new WordInfoPO[k];
		return topWordList.toArray(topWords);

	}

	/**
	 * 先从all_character中选出最大的character； 然后从inverted_index中查找对应的词语（limit 1）
	 * 
	 * @param k
	 * @return
	 */
	@Deprecated
	public WordInfoPO selectTopWordInfo() {
		ConnectionFactory factory = new ConnectionFactory();
		MongoDatabase db = factory.getDBConnection();
		MongoCollection<Document> col = db.getCollection(colName);
		BasicDBObject sortBson = new BasicDBObject(probabilityKey, -1);

		FindIterable<Document> result = col.find().sort(sortBson).limit(1);
		Document doc = result.iterator().next();
		// 从倒排表中查找数据
		String character = doc.getString(idKey);
		String[] tmp = character.split("#");
		int frequency = Integer.valueOf(tmp[0]);
		int length = Integer.valueOf(tmp[1]);
		int indexAvg = Integer.valueOf(tmp[2]);

		col = db.getCollection(invertedIndexTable);
		BasicDBObject filter = new BasicDBObject();
		filter.put(wordFrequencyKey, frequency);
		filter.put(wordLengthKey, length);
		filter.put(indexAvgKey, indexAvg);
		result = col.find(filter).limit(1);

		// 从result中提取结果
		Document wordInfoDoc = result.iterator().next();
		factory.closeConnection();
		return documentToWordInfo(wordInfoDoc);
	}

	/**
	 * 将数据库中查询到的数据转化为Java对象
	 * 
	 * @param doc
	 * @return
	 */
	private WordInfoPO documentToWordInfo(Document doc) {
		String id = doc.getString(idKey);
		@SuppressWarnings("unchecked")
		List<Document> docList = (List<Document>) doc.get(wordInfoKey);
		List<WordInPoetryPO> wordInPoetryList = new ArrayList<WordInPoetryPO>();
		WordInPoetryPO wordInPoetry = null;
		for (Document wordPoetry : docList) {
			wordInPoetry = new WordInPoetryPO();
			String poetryId = wordPoetry.getString(poetryIdKey);
			String author = wordPoetry.getString(authorKey);
			String title = wordPoetry.getString(titleKey);
			String verse = wordPoetry.getString(verseKey);
			wordInPoetry.setPoetryId(poetryId);
			wordInPoetry.setAuthor(author);
			wordInPoetry.setTitle(title);
			wordInPoetry.setVerse(verse);
			wordInPoetryList.add(wordInPoetry);
		}
		WordInfoPO wordInfo = new WordInfoPO();
		wordInfo.setId(id);
		wordInfo.setWordInPoetryList(wordInPoetryList);
		return wordInfo;
	}

	public static void main(String[] args) {
		WordProbability p = new WordProbability();
		long start = 0;
		long end = 0;

		// start = System.currentTimeMillis();
		// p.calAllCharacterProbablity();
		// end = System.currentTimeMillis();
		// System.out.println("用时：" + (end - start));

		int k = 20;
		start = System.currentTimeMillis();
		WordInfoPO[] words = p.selectTopWords(k);
		end = System.currentTimeMillis();
		System.out.println("用时：" + (end - start));
		for (int i = 0; i < words.length; i++) {
			System.out.println(words[i]);
		}
		// BasicWordInfoPO[] basicWords = new BasicWordInfoPO[k];
		// for (int i = 0; i < k; i++) {
		// basicWords[i] = new BasicWordInfoPO(words[i]);
		// }
		// String jsonString = JSONArray.fromObject(basicWords).toString();
		// System.out.println(jsonString);
	}

}
