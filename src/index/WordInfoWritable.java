package index;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

import com.mongodb.ReflectionDBObject;

public class WordInfoWritable extends ReflectionDBObject implements Writable {

	public String getAuthor() {
		return author;
	}

	public String getDynasty() {
		return dynasty;
	}

	public String getTitle() {
		return title;
	}

	public String getVerse() {
		return verse;
	}

	public int getOffset() {
		return offset;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setDynasty(String dynasty) {
		this.dynasty = dynasty;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setVerse(String verse) {
		this.verse = verse;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	private String author;
	private String dynasty;
	private String title;
	private String verse;// 诗句
	private int offset;// 在诗句中出现的位置；

	@Override
	public void readFields(DataInput in) throws IOException {
		author = in.readUTF();
		dynasty = in.readUTF();
		title = in.readUTF();
		verse = in.readUTF();
		offset = in.readInt();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(author);
		out.writeUTF(dynasty);
		out.writeUTF(title);
		out.writeUTF(verse);
		out.writeInt(offset);
	}

	public static WordInfoWritable read(DataInput in) throws IOException {
		WordInfoWritable wordInfo = new WordInfoWritable();
		wordInfo.readFields(in);
		return wordInfo;
	}
}
