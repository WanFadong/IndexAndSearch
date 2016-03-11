package character;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;

import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.util.MongoConfigUtil;

public class CharacterJob {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		String[] collectionNames = { "frequency2", "length", "poetry_index", "verse_index" };
		String[] classNames = { "character.FrequencyMapper", "character.LengthMapper", "character.PoetryIndexMapper",
				"character.VerseIndexMapper" };
		int i = 3;
		long start = System.currentTimeMillis();
		String inputMongoURI = "mongodb://127.0.0.1:27017/allusion.diangu";
		String outputMongoURI = "mongodb://127.0.0.1:27017/allusion." + collectionNames[i];
		Configuration conf = new Configuration();
		MongoConfigUtil.setInputURI(conf, inputMongoURI);
		MongoConfigUtil.setOutputURI(conf, outputMongoURI);

		Job job = Job.getInstance(conf, "Character");

		job.setJarByClass(character.CharacterJob.class);
		job.setMapperClass((Class<? extends Mapper>) Class.forName(classNames[i]));
		job.setReducerClass(CharacterReducer.class);

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
