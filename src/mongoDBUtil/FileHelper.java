package mongoDBUtil;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * 用于将数据从MongoDB中输出到txt文件中;
 * 
 * @author fadongwan
 *
 */
public class FileHelper {
	private static final String DATABASE_NAME = "allusion";

	public static void toFile(String fileName, String collectionName, Map<String, Class<?>> columnMap)
			throws IOException {
		// bufferedReader
		// BufferedReader reader=new BufferedReader(new InputStreamReader(new
		// FileInputStream(fileName)));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
		MongoClient client = new MongoClient("localhost", 27017);
		MongoDatabase database = client.getDatabase(DATABASE_NAME);
		MongoCollection<Document> collection = database.getCollection(collectionName);

		FindIterable<Document> result = collection.find();
		for (Document doc : result) {
			if (columnMap.isEmpty()) {
				break;
			}
			for (Map.Entry<String, Class<?>> entry : columnMap.entrySet()) {
				String name = entry.getKey();
				Class<?> clazz = entry.getValue();
				Object value = doc.get(name, clazz);// 获取属性
				writeToFile(writer, value);
				writeToFile(writer, " ");
			}
			writeToFile(writer, "\r\n");
		}
		client.close();
		writer.close();
	}

	/**
	 * nextLine表示下一个输出的要换行
	 * 
	 * @param writer
	 * @param value
	 * @param nextLine
	 * @throws IOException
	 */
	private static void writeToFile(BufferedWriter writer, Object value) throws IOException {
		writer.write(value.toString());

	}
}
