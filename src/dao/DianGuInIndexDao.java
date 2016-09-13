package dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import model.DianGuPO;

public class DianGuInIndexDao {
	private final String idKey = "_id";
	private final String patternKey = "pattern";
	private final String colName = "diangu_in_index";

	public void insert(List<DianGuPO> dianGuList) {
		ConnectionFactory factory = new ConnectionFactory();
		MongoDatabase db = factory.getDBConnection();
		MongoCollection<Document> col = db.getCollection(colName);

		List<Document> dianGuDocList = new ArrayList<Document>();
		for (DianGuPO dianGu : dianGuList) {
			Document doc = new Document();
			doc.append(idKey, dianGu.getId());
			doc.append(patternKey, dianGu.getPatternList());
			dianGuDocList.add(doc);
		}

		col.insertMany(dianGuDocList);
		factory.closeConnection();
	}

	/**
	 * 1:是；0：不是
	 * 
	 * @param word
	 * @return
	 */
	public int isPattern(String word) {
		ConnectionFactory factory = new ConnectionFactory();
		MongoDatabase db = factory.getDBConnection();
		MongoCollection<Document> col = db.getCollection(colName);

		FindIterable<Document> result = col.find();
		for (Document doc : result) {
			@SuppressWarnings("unchecked")
			List<String> patternList = (List<String>) doc.get(patternKey);
			for (String pattern : patternList) {
				if (word.equals(pattern)) {
					return 1;
				}
			}
		}
		return 0;
	}

}
