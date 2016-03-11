package index;
/**
 *  extends ReflectionDBObject 应该是不需要的，但是没有测试
 */
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import com.mongodb.ReflectionDBObject;

public class WordInfoWritable extends ReflectionDBObject implements Writable {

	public static WordInfoWritable read(DataInput in) throws IOException {
		WordInfoWritable wordInfo = new WordInfoWritable();
		wordInfo.readFields(in);
		return wordInfo;
	}

	private String id;

	private String author;

	private String dynasty;

	private String title;

	private String verse;// 诗句

	private int offset;// 在诗句中出现的位置；

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
	@Override
	public void readFields(DataInput in) throws IOException {
		id = in.readUTF();
		author = in.readUTF();
		dynasty = in.readUTF();
		title = in.readUTF();
		verse = in.readUTF();
		offset = in.readInt();
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

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(id);
		out.writeUTF(author);
		out.writeUTF(dynasty);
		out.writeUTF(title);
		out.writeUTF(verse);
		out.writeInt(offset);
	}
}
