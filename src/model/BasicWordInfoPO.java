package model;

import java.util.ArrayList;
import java.util.List;

public class BasicWordInfoPO {
	private String id;
	private List<BasicWordInPoetryPO> basicWordInPoetryList;
	private double probability;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<BasicWordInPoetryPO> getBasicWordInPoetryList() {
		return basicWordInPoetryList;
	}

	public void setBasicWordInPoetryList(List<BasicWordInPoetryPO> basicWordInPoetryList) {
		this.basicWordInPoetryList = basicWordInPoetryList;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public BasicWordInfoPO(WordInfoPO full) {
		this.id = full.getId();
		List<WordInPoetryPO> poetryFullList = full.getWordInPoetryList();
		List<BasicWordInPoetryPO> basicWordInPoetryList = new ArrayList<BasicWordInPoetryPO>();
		for (WordInPoetryPO poetryFull : poetryFullList) {
			basicWordInPoetryList.add(new BasicWordInPoetryPO(poetryFull));
		}
		this.basicWordInPoetryList = basicWordInPoetryList;
		this.probability = full.getProbability();
	}
}
