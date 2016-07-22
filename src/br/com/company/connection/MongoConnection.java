package br.com.company.connection;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoConnection {

	private static final String DATABASE_NAME="formdb";
	private static final String COLLECTION_NAME="form";
	private static MongoClient serverConnection = null;

	//TODO Thread Safety
	public static DBCollection getCollection() {
		if(serverConnection==null){
			try {
				serverConnection = new MongoClient("localhost",27017);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				throw new RuntimeException("Not able to connect to localhost");
			}
		}		
		DB db = serverConnection.getDB(DATABASE_NAME);
		return db.getCollection(COLLECTION_NAME);
	}
}
