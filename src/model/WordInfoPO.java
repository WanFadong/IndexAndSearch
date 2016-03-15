package model;

import java.util.List;

public class WordInfoPO {
	private String id;
	private List<WordInPoetryPO> wordInPoetryList;
	private int wordLength;
	private int wordFrequency;
	private int indexAvg;

	public int getWordLength() {
		return wordLength;
	}

	public void setWordLength(int wordLength) {
		this.wordLength = wordLength;
	}

	public int getWordFrequency() {
		return wordFrequency;
	}

	public void setWordFrequency(int wordFrequency) {
		this.wordFrequency = wordFrequency;
	}

	public int getIndexAvg() {
		return indexAvg;
	}

	public void setIndexAvg(int indexAvg) {
		this.indexAvg = indexAvg;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<WordInPoetryPO> getWordInPoetryList() {
		return wordInPoetryList;
	}

	public void setWordInPoetryList(List<WordInPoetryPO> wordInPoetryList) {
		this.wordInPoetryList = wordInPoetryList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(id + "||");
		for (int i = 0; i < wordInPoetryList.size(); i++) {
			builder.append(wordInPoetryList.get(i).toString());
		}
		return builder.toString();
	}

}
