package character.probability;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import dao.ConnectionFactory;

/**
 * p(y|x)=d1.d2.d3/b.c^2
 * 
 * p(y|x)=k/b
 * 
 * @author fadongwan
 *
 */

public class ProbabilityCalculator implements ProbabilityCalService {
	// private final int totalWord=2016806;//似乎不需要用到
	// private final long totalPattern = 10243;//是在改变的
	private final String invertedIndexTable = "inverted_index";
	private final String wordFrequencyTable = "frequency";
	private final String wordLengthTable = "length";
	private final String indexAvgTable = "index_avg";
	private final String allCharacterTable = "all_character";
	private final String totalPatternTable = "total_pattern";
	private final String idKey = "_id";
	private final String valueKey = "value";
	private final String wordLengthKey = "word_length";
	private final String wordFrequencyKey = "word_frequency";
	private final String indexAvgKey = "index_avg";
	private final int fieldNum = 3;
	private final int totalPattern = 4944;

	/**
	 * keyword限制： 2~3个词； 必须出现在inverted_index表中
	 * 
	 * @param word
	 * @return
	 */
	public double calProbabilityByWord(String word) {
		int[] values = getWordData(word);
		return calProbability(values);
	}

	public double calProbabilityByCharacter(String allCharacter) {
		int[] values = new int[fieldNum];
		String[] ss = allCharacter.split("#");
		for (int i = 0; i < fieldNum; i++) {
			values[i] = Integer.valueOf(ss[i]);
		}
		return calProbability(values);
	}

	private double calProbability(int[] values) {
		long frequencyNum = getData(wordFrequencyTable, values[0] + "");// d1
		long lengthNum = getData(wordLengthTable, values[1] + "");// d2
		long indexAvgNum = getData(indexAvgTable, values[2] + "");// d3
		long allCharacterNum = getData(allCharacterTable, values[0] + "#" + values[1] + "#" + values[2]);// b
		long patternNum = totalPattern;// c
//		System.out.print(frequencyNum + "|");
//		System.out.print(lengthNum + "|");
//		System.out.print(indexAvgNum + "|");
//		System.out.print(allCharacterNum + "|");
//		System.out.print(patternNum + "|");
		double probability = (frequencyNum * lengthNum * indexAvgNum * 1.0)
				/ (allCharacterNum * patternNum * patternNum * 1.0);

		return probability;
	}

	/**
	 * 获取词语的相关信息； keyword如果在表中不存在，那么返回null；
	 * 
	 * @param keyword
	 * @return
	 */
	private int[] getWordData(String keyword) {
		ConnectionFactory factory = new ConnectionFactory();
		MongoDatabase db = factory.getDBConnection();
		MongoCollection<Document> col = db.getCollection(invertedIndexTable);
		BasicDBObject filter = new BasicDBObject();
		filter.put(idKey, keyword);
		FindIterable<Document> result = col.find(filter);
		int[] values = null;
		for (Document doc : result) {
			values = new int[fieldNum];
			values[0] = doc.getInteger(wordFrequencyKey);
			values[1] = doc.getInteger(wordLengthKey);
			values[2] = doc.getInteger(indexAvgKey);
			break;
		}
		factory.closeConnection();
		return values;
	}

	/**
	 * 从数据库中获取相应数据；
	 * 
	 * @return
	 */
	private int getData(String table, String idValue) {
		ConnectionFactory factory = new ConnectionFactory();
		MongoDatabase db = factory.getDBConnection();
		MongoCollection<Document> col = db.getCollection(table);
		BasicDBObject filter = new BasicDBObject();
		if (!table.equals(allCharacterTable)) {
			filter.put(idKey, Integer.valueOf(idValue));
		} else {
			filter.put(idKey, idValue);
		}
		FindIterable<Document> result = col.find(filter);
		int value = -1;
		for (Document doc : result) {
			value = doc.getInteger(valueKey);
			break;
		}
		factory.closeConnection();
		return value;
	}

	public static void main(String[] args) {
		String[] wordList = { "三山", "凤凰楼" };
		for (int i = 0; i < wordList.length; i++) {
			System.out.println(new ProbabilityCalculator().calProbabilityByWord(wordList[i]));
		}
		// String[] wordList = { "三山", "白雪", "乘鸾", "武陵", "床前", "看月", "地上霜" };
		String[] characterList = { "20#3#0" };
		for (int i = 0; i < characterList.length; i++) {
			System.out.println(new ProbabilityCalculator().calProbabilityByCharacter(characterList[i]));
		}
	}
}
