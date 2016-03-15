package character.count;

/**
 * 对所有分词的几个特征一起wordcount
 * 从inverted_index中获得数据；
 */
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

public class AllCharacterMapper extends Mapper<Object, BSONObject, Text, IntWritable> {
	//private final String wordKey = "_id";
	private final String frequencyKey = "word_frequency";
	private final String lengthKey = "word_length";
	private final String indexAvgKey = "index_avg";

	@Override
	public void map(Object ikey, BSONObject ivalue, Context context) throws IOException, InterruptedException {
		// String word = (String) ivalue.get(wordKey);
		int frequency = (int) ivalue.get(frequencyKey);
		int length = (int) ivalue.get(lengthKey);
		int indexAvg = (int) ivalue.get(indexAvgKey);
		String character = frequency + "#" + length + "#" + indexAvg;
		context.write(new Text(character), new IntWritable(1));
	}

}
