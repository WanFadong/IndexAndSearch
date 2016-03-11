package character;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.util.MongoConfigUtil;

public class CharacterJob {

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		String inputMongoURI = "mongodb://127.0.0.1:27017/allusion.diangu";
		String outputMongoURI = "mongodb://127.0.0.1:27017/allusion.frequency";
		Configuration conf = new Configuration();
		MongoConfigUtil.setInputURI(conf, inputMongoURI);
		MongoConfigUtil.setOutputURI(conf, outputMongoURI);

		Job job = Job.getInstance(conf, "Character");

		job.setJarByClass(character.CharacterJob.class);
		job.setMapperClass(FrequencyMapper.class);
		job.setReducerClass(FrequencyReducer.class);
		
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);

		job.setInputFormatClass(MongoInputFormat.class);
		job.setOutputFormatClass(MongoOutputFormat.class);
		if (job.waitForCompletion(true)) {
			long end = System.currentTimeMillis();
			System.out.println("所用时间:" + (end - start));
			System.exit(0);
		} else {
			System.exit(1);
		}
	}

}
