package index;

import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.BasicBSONList;

import com.mongodb.hadoop.io.BSONWritable;

public class WordReducer extends Reducer<Text, WordInfoWritable, Text, BSONWritable> {

	@Override
	public void reduce(Text word, Iterable<WordInfoWritable> values, Context context)
			throws IOException, InterruptedException {
		// process values
		Iterator<WordInfoWritable> iterator = values.iterator();
		BasicBSONList wordInfoList = new BasicBSONList();
		while (iterator.hasNext()) {
			WordInfoWritable wordInfo = iterator.next();
			// System.out.println(word.toString() + "-------------" +
			// wordInfo.getVerse());
			BSONObject wordInfoObject = new BasicBSONObject();
			wordInfoObject.put("poetry_id", wordInfo.getId());
			wordInfoObject.put("author", wordInfo.getAuthor());
			wordInfoObject.put("dynasty", wordInfo.getDynasty());
			wordInfoObject.put("title", wordInfo.getTitle());
			wordInfoObject.put("verse", wordInfo.getVerse());
			wordInfoObject.put("offset", wordInfo.getOffset());
			wordInfoList.add(wordInfoObject);
		}

		BSONObject outValue = new BasicBSONObject();
		outValue.put("word_info", wordInfoList);

		context.write(word, new BSONWritable(outValue));
	}

}
