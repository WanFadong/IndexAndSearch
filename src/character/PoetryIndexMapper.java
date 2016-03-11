package character;

/**
 * 对典面在唐诗中的位置进行统计
 */
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

import search.MultiWordInfo;
import search.SearchEngine;

public class PoetryIndexMapper extends BasicMapper {

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
				// 输出典面在唐诗中的位置
				int offset = wordInfo.getOffset();
				context.write(new IntWritable(offset), new Text(pattern));
			}
		}
	}

}
