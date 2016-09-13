package model;

import java.util.List;

import org.bson.types.ObjectId;

public class DianGuPO {
	private ObjectId id;
	private List<String> patternList;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public List<String> getPatternList() {
		return patternList;
	}

	public void setPatternList(List<String> patternList) {
		this.patternList = patternList;
	}

}
