package character.count;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

public abstract class BasicMapper extends Mapper<Object, BSONObject, IntWritable, Text> {
	protected static int count=0;

	@Override
	public abstract void map(Object ikey, BSONObject ivalue, Context context) throws IOException, InterruptedException;

	public boolean gogo(){
		count++;
//		if(count>10){
//			return false;
//		}
		return true;
	}
}
