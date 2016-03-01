package search;

import java.util.ArrayList;
import java.util.List;

/**
 * 当搜索关键字是多个词的时候，出现词的诗句就不只是一句了，而是很多句。
 * 
 * @author fadongwan
 *
 */
public class MultiWordInfo {
	private String id;

	private String author;

	private String dynasty;

	private String title;

	private List<String> verses;// 诗句

	private int offset;// 最后一个词在诗句中出现的位置；如果搜索的词没有顺序之分的话，这里也需要用List存放各个词的offset

	public MultiWordInfo() {
		super();
	}

	public MultiWordInfo(String id, String author, String dynasty, String title, List<String> verses, int offset) {
		super();
		this.id = id;
		this.author = author;
		this.dynasty = dynasty;
		this.title = title;
		this.verses = verses;
		this.offset = offset;
	}

	/**
	 * 几个短词进行合并筛选结果的时候，第一个词和null进行合并，需要调用到； 将单个词的搜索结果转化为多个词的搜索结果（将verse变为verses）
	 * 
	 * @param wordInfo
	 */
	public MultiWordInfo(WordInfo wordInfo) {
		this.id = wordInfo.getId();
		this.author = wordInfo.getAuthor();
		this.dynasty = wordInfo.getDynasty();
		this.title = wordInfo.getTitle();
		List<String> verses = new ArrayList<String>();
		verses.add(wordInfo.getVerse());
		this.verses = verses;
		this.offset = wordInfo.getOffset();
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

	public List<String> getVerses() {
		return verses;
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

	public void setVerses(List<String> verses) {
		this.verses = verses;
	}

	/**
	 * 格式化输出
	 */
	@Override
	public String toString() {
		String splitChar = "|";
		String verseSplitChar = "……";
		String str = "作者：" + author + splitChar + "朝代：" + dynasty + splitChar + "题目：" + title + splitChar + "包含搜索词的诗句：";
		for (String verse : verses) {
			str += verse + verseSplitChar;
		}
		str += "\n";
		return str;
	}
}
