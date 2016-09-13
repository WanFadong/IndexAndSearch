package dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import model.DianGuPO;

public class DianGuDao {
	private final String colName = "diangu";
	private final String idKey = "_id";
	private final String patternKey = "pattern";

	/**
	 * 只返回id和典面；
	 * 
	 * @return
	 */
	public List<DianGuPO> findAll() {
		ConnectionFactory factory = new ConnectionFactory();
		MongoDatabase db = factory.getDBConnection();
		MongoCollection<Document> col = db.getCollection(colName);

		FindIterable<Document> result = col.find();
		List<DianGuPO> dianGuList = new ArrayList<DianGuPO>();
		for (Document dianGu : result) {
			ObjectId id = dianGu.getObjectId(idKey);
			@SuppressWarnings("unchecked")
			List<String> patternList = (List<String>) dianGu.get(patternKey);
			DianGuPO dianGuPO = new DianGuPO();
			dianGuPO.setId(id);
			dianGuPO.setPatternList(patternList);
			dianGuList.add(dianGuPO);
		}
		factory.closeConnection();
		return dianGuList;
	}
	
}
