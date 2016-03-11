package character;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

public class LengthMapper extends BasicMapper{

	@Override
	public void map(Object ikey, BSONObject ivalue, Mapper<Object, BSONObject, IntWritable, Text>.Context context)
			throws IOException, InterruptedException {
		if (!gogo()) {
			return;
		}
		String patternKey="pattern";
		@SuppressWarnings("unchecked")
		List<String> patternList=(List<String>) ivalue.get(patternKey);
		Iterator<String> it=patternList.iterator();
		while(it.hasNext()){
			String pattern=it.next();
			int length=pattern.length();
			context.write(new IntWritable(length), new Text(pattern));
		}
	}

}
