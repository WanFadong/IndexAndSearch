package search;

public class WordInfo {
	private String id;

	private String author;

	private String dynasty;

	private String title;

	private String verse;// 诗句

	private int offset;// 在诗句中出现的位置；

	public WordInfo() {
		super();
	}

	public WordInfo(String id, String author, String dynasty, String title, String verse, int offset) {
		super();
		this.id = id;
		this.author = author;
		this.dynasty = dynasty;
		this.title = title;
		this.verse = verse;
		this.offset = offset;
	}

	public String getAuthor() {
		return author;
	}

	public String getDynasty() {
		return dynasty;
	}

	public String getId() {
		return id;
	}

	public int getOffset() {
		return offset;
	}

	public String getTitle() {
		return title;
	}

	public String getVerse() {
		return verse;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setDynasty(String dynasty) {
		this.dynasty = dynasty;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setVerse(String verse) {
		this.verse = verse;
	}

}
