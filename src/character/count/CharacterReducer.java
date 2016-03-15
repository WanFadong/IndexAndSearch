package character.count;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CharacterReducer extends Reducer<IntWritable, Text, IntWritable, IntWritable> {

	@Override
	public void reduce(IntWritable _key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		// process values
		int num = 0;
		Iterator<Text> it = values.iterator();
		while (it.hasNext()) {
			it.next();
			num++;
		}
		// 包装

		context.write(_key, new IntWritable(num));

	}

}
