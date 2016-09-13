package model;

public class BasicWordInPoetryPO {
	private String poetryId;
	private String author;
	private String title;
	private String verse;

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

	public String getVerse() {
		return verse;
	}

	public void setVerse(String verse) {
		this.verse = verse;
	}

	public BasicWordInPoetryPO(WordInPoetryPO wordInPoetryPO) {
		this.poetryId = wordInPoetryPO.getPoetryId();
		this.author = wordInPoetryPO.getAuthor();
		this.title = wordInPoetryPO.getTitle();
		this.verse = wordInPoetryPO.getVerse();
	}
}
