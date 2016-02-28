package search;

import java.util.ArrayList;
import java.util.List;

public class SearchWords {
	private List<List<String>> words;
	private int size;

	/**
	 * 讲搜索关键词变为可用的词组；
	 * 
	 * @param keyword
	 */
	public SearchWords(String keyword) {
		String[] longWords = keyword.split("\\+");
		this.size = longWords.length;
		words = new ArrayList<List<String>>();
		for (int i = 0; i < longWords.length; i++) {
			List<String> shortWords = new ArrayList<String>();
			String longWord = longWords[i];
			int wordLength = longWord.length();
			int j = 0;
			for (; j < wordLength - 3; j = j + 2) {
				String shortWord = longWord.substring(j, j + 2);
				shortWords.add(shortWord);
			}
			shortWords.add(longWord.substring(j, wordLength));
			words.add(shortWords);
		}
	}

	public List<String> getShortWords(int index) {
		return words.get(index);
	}

	public int getSize() {
		return size;
	}
}
