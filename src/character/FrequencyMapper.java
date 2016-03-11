package character;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

import search.MultiWordInfo;
import search.SearchEngine;

public class FrequencyMapper extends BasicMapper {

	@Override
	public void map(Object ikey, BSONObject ivalue, Context context) throws IOException, InterruptedException {
		if (!gogo()) {
			return;
		}
		// 获取典面;
		String patternWord = "pattern";
		@SuppressWarnings("unchecked")
		List<String> patternList = (List<String>) ivalue.get(patternWord);
		Iterator<String> it = patternList.iterator();
		String pattern = null;
		List<MultiWordInfo> result = null;
		int num = 0;
		while (it.hasNext()) {
			pattern = it.next();

			// 搜索结果;
			result = SearchEngine.search(pattern);
			num = result.size();

			// 输出
			context.write(new IntWritable(num), new Text(pattern));
		}
	}

}
