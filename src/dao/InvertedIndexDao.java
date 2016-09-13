package dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import model.WordInPoetryPO;
import model.WordInfoPO;

/**
 * 获取数据的时候要考虑isPattern为null的情况
 * 
 * @author fadongwan
 *
 */
public class InvertedIndexDao {
	private final String colName = "inverted_index";
	private final String idKey = "_id";
	private final String wordLengthKey = "word_length";
	private final String wordFrequencyKey = "word_frequency";
	private final String indexAvgKey = "index_avg";
	private final String isPatternKey = "is_pattern";
	private final String wordInfoKey = "word_info";
	private final String poetryIdKey = "poetry_id";
	private final String authorKey = "author";
	private final String dynastryKey = "dynasty";
	private final String titleKey = "title";
	private final String verseKey = "verse";
	private final String offsetKey = "offset";
	private final String isPatternVerseKey = "is_pattern_verse";
	private final String setKey = "$set";

	/**
	 * 更新，只更新是否是典面；
	 * 
	 * @param wordInfo
	 */
	public void update(WordInfoPO wordInfo) {
		String id = wordInfo.getId();
		int isPattern = wordInfo.getIsPattern();
		int isPatternVerse = wordInfo.getIsPattern();

		ConnectionFactory factory = new ConnectionFactory();
		MongoDatabase db = factory.getDBConnection();
		MongoCollection<Document> col = db.getCollection(colName);

		BasicDBObject filter = new BasicDBObject();
		filter.put(isPatternKey, isPattern);
		filter.put(isPatternVerseKey, isPatternVerse);
		BasicDBObject data = new BasicDBObject();
		data.put(setKey, new BasicDBObject(idKey, id));
		col.updateOne(filter, data);
		factory.closeConnection();
	}

	/**
	 * probability默认为0
	 * 
	 * @return
	 */
	@Deprecated
	public List<WordInfoPO> findAll() {
		ConnectionFactory factory = new ConnectionFactory();
		MongoDatabase db = factory.getDBConnection();
		MongoCollection<Document> collection = db.getCollection(colName);
		FindIterable<Document> result = collection.find();

		List<WordInfoPO> wordInfoList = new ArrayList<WordInfoPO>();
		int count = 0;
		for (Document doc : result) {
			count++;
			if (count % 10000 == 0) {
				System.out.println("正在装载第：" + count + "条数据");
			}
			WordInfoPO wordInfo = new WordInfoPO();
			wordInfo.setId(doc.getString(idKey));
			wordInfo.setWordFrequency(doc.getInteger(wordFrequencyKey));
			wordInfo.setWordLength(doc.getInteger(wordLengthKey));
			wordInfo.setIndexAvg(doc.getInteger(indexAvgKey));
			wordInfo.setIsPattern(doc.getInteger(isPatternKey, 0));
			wordInfo.setProbability(0);
			wordInfo.setWordInPoetryList(setWordInPoetryList(doc));
			wordInfoList.add(wordInfo);
		}

		factory.closeConnection();
		return wordInfoList;
	}

	public List<WordInfoPO> findMany(int limit, int skip) {
		ConnectionFactory factory = new ConnectionFactory();
		MongoDatabase db = factory.getDBConnection();
		MongoCollection<Document> collection = db.getCollection(colName);
		FindIterable<Document> result = collection.find().limit(limit).skip(skip);

		List<WordInfoPO> wordInfoList = new ArrayList<WordInfoPO>();
		int count = 0;
		for (Document doc : result) {
			count++;
			if (count % 10000 == 0) {
				System.out.println("正在装载第：" + count + "条数据");
			}
			WordInfoPO wordInfo = new WordInfoPO();
			wordInfo.setId(doc.getString(idKey));
			wordInfo.setWordFrequency(doc.getInteger(wordFrequencyKey));
			wordInfo.setWordLength(doc.getInteger(wordLengthKey));
			wordInfo.setIndexAvg(doc.getInteger(indexAvgKey));
			wordInfo.setIsPattern(doc.getInteger(isPatternKey, 0));
			wordInfo.setProbability(0);
			wordInfo.setWordInPoetryList(setWordInPoetryList(doc));
			wordInfoList.add(wordInfo);
		}

		factory.closeConnection();
		return wordInfoList;
	}

	private List<WordInPoetryPO> setWordInPoetryList(Document doc) {
		@SuppressWarnings("unchecked")
		List<Document> poetryList = (List<Document>) doc.get(wordInfoKey);
		List<WordInPoetryPO> poetryPOList = new ArrayList<WordInPoetryPO>();
		for (Document poetry : poetryList) {
			WordInPoetryPO poetryPO = new WordInPoetryPO();
			poetryPO.setPoetryId(poetry.getString(poetryIdKey));
			poetryPO.setAuthor(poetry.getString(authorKey));
			poetryPO.setDynasty(poetry.getString(dynastryKey));
			poetryPO.setIsPatternVerse(poetry.getInteger(isPatternVerseKey, 0));
			poetryPO.setOffset(poetry.getInteger(offsetKey));
			poetryPO.setVerse(poetry.getString(verseKey));
			poetryPO.setTitle(poetry.getString(titleKey));
			poetryPOList.add(poetryPO);
		}
		return poetryPOList;
	}
}
