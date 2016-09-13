package character.init;

import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import dao.ConnectionFactory;

public class DianMianCount {
	private final String colName = "diangu_in_index";
	private final String patternKey = "pattern";
	private final String totalPatternTable = "total_pattern";

	private int countDianMian() {
		int count = 0;
		ConnectionFactory factory = new ConnectionFactory();
		MongoDatabase db = factory.getDBConnection();
		MongoCollection<Document> col = db.getCollection(colName);
		@SuppressWarnings("unchecked")
		FindIterable<Document> result = col.find();
		for (Document doc : result) {
			@SuppressWarnings("unchecked")
			List<String> patternList = (List<String>) doc.get(patternKey);
			int size = patternList.size();
			count += size;
		}
		col = db.getCollection(totalPatternTable);
		col.insertOne(new Document("value", count));
		factory.closeConnection();
		return count;
	}

	public static void main(String[] args) {
		System.out.println(new DianMianCount().countDianMian());
	}

}
