package br.com.company.dao;

import java.util.List;

import com.mongodb.DBObject;

public interface Dao {
	
	public List<DBObject> listForms();
	
	public DBObject findFormById(String id);
	
	public boolean insertForm(String fields);

	public boolean updateForm(String id, DBObject dbObject);
	
	public boolean deleteForm(DBObject dbObject);
	
	public boolean addFieldOnForm(String id, DBObject dbObject);
}
