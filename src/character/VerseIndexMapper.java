package character;

/**
 * 统计典面在诗句中的位置;
 * 不完善的地方:如果pattern是"+"的,那么处理有问题;
 */
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

import search.MultiWordInfo;
import search.SearchEngine;

public class VerseIndexMapper extends BasicMapper {

	@Override
	public void map(Object ikey, BSONObject ivalue, Mapper<Object, BSONObject, IntWritable, Text>.Context context)
			throws IOException, InterruptedException {
		if (!gogo()) {
			return;
		}
		String patternKey = "pattern";
		@SuppressWarnings("unchecked")
		List<String> patternList = (List<String>) ivalue.get(patternKey);
		Iterator<String> it = patternList.iterator();
		while (it.hasNext()) {
			String pattern = it.next();
			List<MultiWordInfo> wordInfoList = SearchEngine.search(pattern);
			for (MultiWordInfo wordInfo : wordInfoList) {
				// 输出典面在诗句中的位置.一般没有分开的词,所以只需要看是在第一句诗的什么位置
				List<String> verses = wordInfo.getVerses();
				String verse = verses.get(0);
				int index = verse.indexOf(pattern);
				context.write(new IntWritable(index), new Text(pattern));
			}
		}
	}

}
