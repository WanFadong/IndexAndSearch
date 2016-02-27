package index;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.util.MongoConfigUtil;

public class WordJob {

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		Configuration conf = new Configuration();
		MongoConfigUtil.setInputURI(conf, "mongodb://127.0.0.1:27017/allusion.poetry");
		MongoConfigUtil.setOutputURI(conf, "mongodb://127.0.0.1:27017/allusion.inverted_index");

		Job job = Job.getInstance(conf, "index");

		job.setJarByClass(WordJob.class);
		job.setMapperClass(WordMapper.class);
		job.setReducerClass(WordReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(WordInfoWritable.class);

		job.setInputFormatClass(MongoInputFormat.class);
		job.setOutputFormatClass(MongoOutputFormat.class);
		if (job.waitForCompletion(true)) {
			long end = System.currentTimeMillis();
			System.out.println("时间：" + (end - start));
			System.exit(0);
		} else {
			System.exit(1);
		}
	}

}
