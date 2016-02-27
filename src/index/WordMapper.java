package index;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

public class WordMapper extends Mapper<Object, BSONObject, Text, WordInfoWritable> {
	private final String defaultDynasty = "唐";
//	private static int count = 0;

	@Override
	public void map(Object ikey, BSONObject ivalue, Context context) throws IOException, InterruptedException {
//		count++;
//		if (count > 100) {
//			return;
//		}
		// 获取信息,组装相应数据；
		String content = (String) ivalue.get("content");
		String title = (String) ivalue.get("title");
		String author = (String) ivalue.get("author");
		String dynasty = defaultDynasty;
		WordInfoWritable wordInfo = new WordInfoWritable();
		wordInfo.setAuthor(author);
		wordInfo.setDynasty(dynasty);
		wordInfo.setTitle(title);

		// 进行分词
		// 分句
		int length = content.length();
		int start = 0;
		int end = 0;
		for (int i = 0; i < length; i++) {
			String c = content.substring(i, i + 1);
			if (isPunctuation(c)) {
				String sentence = content.substring(start, end);
				wordInfo.setVerse(sentence);
				segmentWord(start, sentence, wordInfo, context);
				end++;
				start = end;
			} else {
				end++;
			}
		}
		// 处理最后一句话（考虑到不是以标点符号结尾的诗句）
		String sentence = content.substring(start, end);
		wordInfo.setVerse(sentence);
		segmentWord(start, sentence, wordInfo, context);
	}

	private void segmentWord(int startIndex, String sentence, WordInfoWritable wordInfo, Context context)
			throws IOException, InterruptedException {
		// 如果有连续两个标点符号，sentence长度为0
		int length = sentence.length();
		if (length <= 1) {
			return;
		}
		// 2个字符
		for (int i = 0; i < length - 1; i++) {
			String word = sentence.substring(i, i + 2);
			// 写出
			int offset = startIndex + i;
			wordInfo.setOffset(offset);
			context.write(new Text(word), wordInfo);
		}
		// 3个字符
		for (int i = 0; i < length - 2; i++) {
			String word = sentence.substring(i, i + 3);

			// 写出
			int offset = startIndex + i;
			wordInfo.setOffset(offset);
			context.write(new Text(word), wordInfo);
		}
	}

	private boolean isPunctuation(String c) {
		final String[] punctuations = { "。", "？", "！", "，", "、", "；", "：", "（", "）", "《", "》", " ", "-", "【", "□", ")",
				"(", "#", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "", "*", "\\", ">", "<", "", ",", ".", "A",
				"a", "B", "b", "C", "c", "D", "d", "E", "e", "F", "f", "G", "g", "H", "h", "i", "I", "J", "j", "K", "k",
				"L", "l", "M", "m", "N", "n", "O", "o", "P", "p", "Q", "q", "R", "r", "S", "s", "t", "T", "U", "u", "V",
				"v", "W", "w", "X", "x", "Y", "y", "Z", "z", "“", "‘", "”" };
		int length = punctuations.length;
		for (int i = 0; i < length; i++) {
			if (punctuations[i].compareTo(c) == 0) {
				return true;
			}
		}
		return false;
	}

}
