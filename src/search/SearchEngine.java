package search;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class SearchEngine {
	private static final int threshold = 20;
	private static final int minThreshold = 2;

	public static List<WordInfo> search(String keyword) {
		SearchWords words = new SearchWords(keyword);
		return search(words);
	}

	/**
	 * 搜索结果为空返回空数组
	 * 
	 * @param words
	 * @return
	 */
	private static List<WordInfo> search(SearchWords words) {
		List<WordInfo> wordInfoList = null;
		List<WordInfo> newWordInfoList;
		int size = words.getSize();
		for (int i = 0; i < size; i++) {
			List<String> shortWords = words.getShortWords(i);
			for (int j = 0; j < shortWords.size(); j++) {
				String shortWord = shortWords.get(j);
				newWordInfoList = searchFromMongo(shortWord);
				if (j == 0) {
					wordInfoList = filter(wordInfoList, newWordInfoList, false);
				} else {
					wordInfoList = filter(wordInfoList, newWordInfoList, true);
				}
				if (wordInfoList.size() == 0) {
					return wordInfoList;// 返回空数组
				}
			}
		}
		return wordInfoList;
	}

	/**
	 * 将两个结果筛选成一个结果 word在一起的，offset间隔2；word不在一起的，offset间隔20；
	 * list1在前，list2在后，前后敏感
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	private static List<WordInfo> filter(List<WordInfo> list1, List<WordInfo> list2, boolean together) {
		if (list1 == null) {
			return list2;
		}
		List<WordInfo> returnList = new ArrayList<WordInfo>();
		for (int i = 0; i < list1.size(); i++) {
			WordInfo word1 = list1.get(i);
			String poetryId1 = word1.getId();
			int offset1 = word1.getOffset();
			for (int j = 0; j < list2.size(); j++) {
				WordInfo word2 = list2.get(j);
				String poetryId2 = word2.getId();
				int offset2 = word2.getOffset();
				if (poetryId2.equals(poetryId1)) {
					int distance = offset2 - offset1;
					if (together) {
						if (distance == minThreshold) {
							returnList.add(word1);
						}
					} else {
						if (distance <= threshold) {
							returnList.add(word1);
						}
					}
				}
			}
		}
		return returnList;
	}

	/**
	 * 从mongoDB中搜索关键字，返回List 没有结果返回size=0；
	 */
	private static List<WordInfo> searchFromMongo(String word) {
		List<WordInfo> wordInfoList = new ArrayList<WordInfo>();

		MongoClient client = new MongoClient("localhost", 27017);
		String databaseName = "allusion";
		MongoDatabase database = client.getDatabase(databaseName);
		String collectionName = "inverted_index";
		MongoCollection<Document> collection = database.getCollection(collectionName);

		String idKey = "_id";
		String wordInfoKey = "word_info";
		String poetryIdKey = "poetry_id";
		String authorKey = "author";
		String dynastyKey = "dynasty";
		String titleKey = "title";
		String verseKey = "verse";
		String offsetKey = "offset";
		Document document = new Document(idKey, word);
		FindIterable<Document> iterator = collection.find(document);

		for (Document doc : iterator) {
			List<Document> wordInfoDocList = (List<Document>) doc.get(wordInfoKey);
			// System.out.println(wordInfoDocList.size());
			for (Document wordInfoDoc : wordInfoDocList) {
				String poetryId = wordInfoDoc.getString(poetryIdKey);
				String author = wordInfoDoc.getString(authorKey);
				String dynasty = wordInfoDoc.getString(dynastyKey);
				String title = wordInfoDoc.getString(titleKey);
				String verse = wordInfoDoc.getString(verseKey);
				int offset = wordInfoDoc.getInteger(offsetKey);
				// System.out.println(verse);
				WordInfo wordInfo = new WordInfo(poetryId, author, dynasty, title, verse, offset);
				wordInfoList.add(wordInfo);
			}
		}
		return wordInfoList;
	}

	public static void main(String[] args) {
		String word = "";
		List<WordInfo> wordInfoList = search(word);
		System.out.println(wordInfoList.size());
		for (int i = 0; i < wordInfoList.size(); i++) {
			System.out.println(wordInfoList.get(i).getVerse());
		}
	}
}
