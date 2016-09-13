package model;

public class WordInPoetryPO {
	private String poetryId;
	private String author;
	private String title;
	private String dynasty;
	private String verse;
	private int offset;
	private int isPatternVerse;

	public int getIsPatternVerse() {
		return isPatternVerse;
	}

	public void setIsPatternVerse(int isPatternVerse) {
		this.isPatternVerse = isPatternVerse;
	}

	public String getPoetryId() {
		return poetryId;
	}

	public void setPoetryId(String poetryId) {
		this.poetryId = poetryId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDynasty() {
		return dynasty;
	}

	public void setDynasty(String dynasty) {
		this.dynasty = dynasty;
	}

	public String getVerse() {
		return verse;
	}

	public void setVerse(String verse) {
		this.verse = verse;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return "作者：" + author + "；题目：" + title + "；诗句：" + verse + "\r\n";
	}
}
