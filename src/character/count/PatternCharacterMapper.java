package character.count;

/**
 * 对所有典面的几个特征一起wordcount
 * 从diangu_in_index中获得数据；
 */
import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

import search.MultiWordInfo;
import search.SearchEngine;

public class PatternCharacterMapper extends Mapper<Object, BSONObject, Text, IntWritable> {
	// private final String wordKey = "_id";
	private final String patternKey = "pattern";

	@Override
	public void map(Object ikey, BSONObject ivalue, Context context) throws IOException, InterruptedException {
		List<String> patternList = (List<String>) ivalue.get(patternKey);
		for (String pattern : patternList) {
			List<MultiWordInfo> wordInfoList = SearchEngine.search(pattern);
			// 计算需要的数据
			int frequency = wordInfoList.size();
			int length = pattern.length();
			// 计算平均位置
			int indexAvg = 0;
			int sum = 0;
			for (MultiWordInfo wordInfo : wordInfoList) {
				sum += wordInfo.getVerses().get(0).indexOf(pattern);
			}
			indexAvg = (int) Math.round(sum * 1.0 / frequency);
			String character = frequency + "#" + length + "#" + indexAvg;
			context.write(new Text(character), new IntWritable(1));
		}
	}

}
