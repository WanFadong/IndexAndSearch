package character.count;

/**
 * 用于计算每个典面在句中出现的平均位置；
 */
import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

import search.MultiWordInfo;
import search.SearchEngine;

public class IndexAvgMapper extends BasicMapper {
	private final String patternKey = "pattern";

	@Override
	public void map(Object ikey, BSONObject ivalue, Mapper<Object, BSONObject, IntWritable, Text>.Context context)
			throws IOException, InterruptedException {
		if (!gogo()) {
			return;
		}
		@SuppressWarnings("unchecked")
		List<String> patternList = (List<String>) ivalue.get(patternKey);
		for (String pattern : patternList) {
			List<MultiWordInfo> wordInfoList = SearchEngine.search(pattern);
			int sum = 0;
			int size = wordInfoList.size();
			for (MultiWordInfo wordInfo : wordInfoList) {
				List<String> verseList = wordInfo.getVerses();
				sum = sum + verseList.get(0).indexOf(pattern);
			}
			int indexAvg = (int) Math.round(sum * 1.0 / size);
			context.write(new IntWritable(indexAvg), new Text(pattern));
		}
	}

}
