package character.init;

import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import dao.ConnectionFactory;
import dao.DianGuInIndexDao;
import dao.InvertedIndexDao;
import model.WordInPoetryPO;
import model.WordInfoPO;

/**
 * 用于更新inverted_index表的相关内容;增加词频、词长、句中位置字段；
 * 
 * @author fadongwan
 *
 */
public class IndexModification {

	private final String colName = "inverted_index";
	private final String idKey = "_id";
	private final String wordInfoKey = "word_info";
	private final String verseKey = "verse";
	private final String wordLengthKey = "word_length";
	private final String wordFrequencyKey = "word_frequency";
	private final String indexAvgKey = "index_avg";
	private final String setKey = "$set";
	private final String isPatternKey = "is_pattern";
	private final String isPatternVerseKey = "is_pattern_verse";
	private final int totalWord = 2016806;

	private void modify() {
		int count = 0;
		ConnectionFactory factory = new ConnectionFactory();
		MongoDatabase db = factory.getDBConnection();
		MongoCollection<Document> collection = db.getCollection(colName);
		FindIterable<Document> result = collection.find();
		for (Document doc : result) {
			count++;
			if (count % 10000 == 0) {
				// System.out.println(count);
			}
			// 获取/计算相关数据
			String word = doc.getString(idKey);
			int wordLength = word.length();
			@SuppressWarnings("unchecked")
			List<Document> wordInfoList = (List<Document>) doc.get(wordInfoKey);
			int wordFrequency = wordInfoList.size();
			int sum = 0;
			for (Document wordInfo : wordInfoList) {
				String verse = wordInfo.getString(verseKey);
				int index = verse.indexOf(word);
				sum = sum + index;
			}
			int avg = (int) Math.round(sum * 1.0 / wordFrequency);

			// 写入到数据库
			BasicDBObject filter = new BasicDBObject(idKey, word);
			BasicDBObject innerData = new BasicDBObject();
			innerData.put(wordLengthKey, wordLength);
			innerData.put(wordFrequencyKey, wordFrequency);
			innerData.put(indexAvgKey, avg);
			BasicDBObject data = new BasicDBObject(setKey, innerData);
			collection.updateMany(filter, data);
		}
		factory.closeConnection();
	}

	private void initPattern() {
		int count = 0;

		List<WordInfoPO> wordInfoList = new InvertedIndexDao().findAll();
		Iterator<WordInfoPO> it = wordInfoList.iterator();
		while (it.hasNext()) {
			count++;
			if (count % 10000 == 0) {
				System.out.println(count);
			}
			WordInfoPO wordInfoPO = it.next();
			// 获取/计算相关数据
			String word = wordInfoPO.getId();

			// 判断是否是典面；
			int isPattern = new DianGuInIndexDao().isPattern(word);

			// 写入到数据库
			WordInfoPO wordInfo = new WordInfoPO();
			wordInfo.setId(word);
			wordInfo.setIsPattern(isPattern);
			List<WordInPoetryPO> wordInPoetryPOList = wordInfoPO.getWordInPoetryList();
			for (WordInPoetryPO wordInPoetryPO : wordInPoetryPOList) {
				wordInPoetryPO.setIsPatternVerse(isPattern);
			}
			wordInfo.setWordInPoetryList(wordInPoetryPOList);// ?是否需要设置，传递的是引用还是值
			// System.out.println(wordInfo);
			new InvertedIndexDao().update(wordInfo);
		}

	}

	public static void main(String[] args) {
		// new IndexModification().modify();
		new IndexModification().initPattern();
	}

}
