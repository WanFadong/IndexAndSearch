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

	public static List<MultiWordInfo> search(String keyword) {
		SearchWords words = new SearchWords(keyword);
		return search(words);
	}

	/**
	 * 搜索结果为空返回空数组
	 * 
	 * @param words
	 * @return
	 */
	private static List<MultiWordInfo> search(SearchWords words) {
		int size = words.getSize();// 分词的数量
		List<MultiWordInfo> multiWordInfoList = null;
		for (int i = 0; i < size; i++) {
			List<String> shortWords = words.getShortWords(i);// 一个长词分割
			List<WordInfo> wordInfoList = null;// 用于存放一个长词的最终搜索结果
			List<WordInfo> newWordInfoList;// 临时存放一个短词的搜索结果，用于合并筛选
			for (int j = 0; j < shortWords.size(); j++) {
				String shortWord = shortWords.get(j);
				newWordInfoList = searchFromMongo(shortWord);
				wordInfoList = intraWordFilter(wordInfoList, newWordInfoList);
			}
			multiWordInfoList = interWordFilter(multiWordInfoList, wordInfoList);
		}
		return multiWordInfoList;
	}

	/**
	 * 一个词（长度超过3）内两个分词搜索结果的合并筛选； 注意： 1.如果list为null，直接返回list2；（第一个词）
	 * 2.要求offset相差为2； 3.list1要在list2前面
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	private static List<WordInfo> intraWordFilter(List<WordInfo> list1, List<WordInfo> list2) {
		if (list1 == null) {
			return list2;
		}
		List<WordInfo> returnList = new ArrayList<WordInfo>();
		// 遍历list1
		for (int i = 0; i < list1.size(); i++) {
			WordInfo word1 = list1.get(i);
			String poetryId1 = word1.getId();
			int offset1 = word1.getOffset();
			// 遍历list2
			for (int j = 0; j < list2.size(); j++) {
				WordInfo word2 = list2.get(j);
				String poetryId2 = word2.getId();
				int offset2 = word2.getOffset();
				if (poetryId2.equals(poetryId1) && (offset2 - offset1) == minThreshold) {
					returnList.add(word1);// 如果是同一首诗中，且offset相差为2的（认为是同一句诗）
				}
			}
		}
		return returnList;

	}

	/**
	 * 两个分开的词搜索结果的合并筛选 ;注意：list1和list2中搜索的词有顺序之分； 如果list1为空，直接返回list2中的数据（变形后）；
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	private static List<MultiWordInfo> interWordFilter(List<MultiWordInfo> list1, List<WordInfo> list2) {
		List<MultiWordInfo> returnList = new ArrayList<MultiWordInfo>();
		if (list1 == null) {
			for (WordInfo wordInfo : list2) {
				returnList.add(new MultiWordInfo(wordInfo));
			}
			return returnList;
		}

		for (int i = 0; i < list1.size(); i++) {
			MultiWordInfo multiWordInfo = list1.get(i);
			String poetryId1 = multiWordInfo.getId();
			int offset1 = multiWordInfo.getOffset();
			for (int j = 0; j < list2.size(); j++) {
				WordInfo wordInfo = list2.get(j);
				String poetryId2 = wordInfo.getId();
				int offset2 = wordInfo.getOffset();
				if (poetryId2.equals(poetryId1) && (offset2 - offset1) < threshold) {
					List<String> verses = multiWordInfo.getVerses();
					verses.add(wordInfo.getVerse());
					multiWordInfo.setVerses(verses);
					multiWordInfo.setOffset(offset2);
					returnList.add(multiWordInfo);
					// 这里可以break，但考虑到一个词有可能在一首诗中出现多次
				}
			}
		}
		return returnList;
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
		String word = "床前+疑是";
		List<MultiWordInfo> multiWordInfoList = search(word);
		System.out.println(multiWordInfoList.size());
		for (int i = 0; i < multiWordInfoList.size(); i++) {
			System.out.println(multiWordInfoList.get(i));
		}
	}
}
