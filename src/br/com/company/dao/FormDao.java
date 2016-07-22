package br.com.company.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

import br.com.company.connection.MongoConnection;

public class FormDao implements Dao {

	private static final DBCollection COLLECTION = MongoConnection.getCollection();
	private Integer sizeCurrentCollection;

	public FormDao() {
		sizeCurrentCollection = COLLECTION.find().count();
	}

	@Override
	public List<DBObject> listForms() {
		List<DBObject> cursors = new ArrayList<DBObject>();
		DBCursor allResults = COLLECTION.find();
		while (allResults.hasNext()) {
			cursors.add(allResults.next());
		}
		return cursors;
	}

	@Override
	public DBObject findFormById(String id) {
		DBObject dbObject = null;
		BasicDBObject searchObject = new BasicDBObject();
		searchObject.put("_id", new ObjectId(id));
		DBCursor resultSubset = COLLECTION.find(searchObject);

		while (resultSubset.hasNext()) {
			dbObject = resultSubset.next();
		}
		return dbObject;
	}

	@Override
	public boolean insertForm(String fields) {
		DBObject dbObject = (DBObject) JSON.parse(fields);
		COLLECTION.insert(dbObject);
		return sizeCurrentCollection <= COLLECTION.find().count();
	}

	@Override
	public boolean updateForm(String id, DBObject dbObject) {
		// BasicDBObject basicDBObject = (BasicDBObject) dbObject;
		return COLLECTION.update(new BasicDBObject().append("_id", new ObjectId(id)), dbObject).getN() != 0;
	}

	@Override
	public boolean deleteForm(DBObject dbObject) {
		return COLLECTION.remove(dbObject).getN() != 0;
	}

	@Override
	public boolean addFieldOnForm(String id, DBObject dbObject) {
		try {
			COLLECTION.save(dbObject);
			return true;
		} catch (MongoException exception) {
			return false;
		}
	}

}
