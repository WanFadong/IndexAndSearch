package model;

import java.util.List;

public class WordInfoPO {
	private String id;
	private List<WordInPoetryPO> wordInPoetryList;
	private int wordLength;
	private int wordFrequency;
	private int indexAvg;
	private double probability;
	private int isPattern;// 0,1

	public int getIsPattern() {
		return isPattern;
	}

	public void setIsPattern(int isPattern) {
		this.isPattern = isPattern;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

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
		builder.append("词语：" + id + "；");
		builder.append("评分：" + probability + "\r\n");
		// builder.append("特征：" + wordFrequency + "#" + wordLength + "#" +
		// indexAvg + "\r\n");
		// builder.append("典面："+isPattern+"||");
		for (int i = 0; i < wordInPoetryList.size(); i++) {
			builder.append(wordInPoetryList.get(i).toString());
		}

		return builder.toString();
	}

}
