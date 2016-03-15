package mongoDBUtil;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class ConnectionFactory {
	private final String dbName = "allusion";
	private MongoClient client;
	private MongoDatabase db;

	public MongoDatabase getDBConnection() {
		client = new MongoClient("localhost", 27017);
		db = client.getDatabase(dbName);
		return db;
	}

	public void closeConnection() {
		client.close();
	}
}
