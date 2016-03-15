package character.init;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import mongoDBUtil.ConnectionFactory;

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

	private void modify() {
		int count = 0;
		ConnectionFactory factory = new ConnectionFactory();
		MongoDatabase db = factory.getDBConnection();
		MongoCollection<Document> collection = db.getCollection(colName);
		FindIterable<Document> result = collection.find();
		for (Document doc : result) {
			count++;
			if (count % 10000 == 0) {
				System.out.println(count);
			}
			// 获取/计算相关数据
			String word = doc.getString(idKey);
			int wordLength = word.length();
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
		ConnectionFactory factory = new ConnectionFactory();
		MongoDatabase db = factory.getDBConnection();
		MongoCollection<Document> collection = db.getCollection(colName);
		FindIterable<Document> result = collection.find();
		for (Document doc : result) {
			count++;
			if (count % 10000 == 0) {
				System.out.println(count);
			}
			// 获取/计算相关数据
			String word = doc.getString(idKey);
			// 判断是否是典面；
			boolean isPattern;

			int wordLength = word.length();
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

	public static void main(String[] args) {
		new IndexModification().modify();
	}

}
