package br.com.company.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import br.com.company.connection.MongoConnection;
import br.com.company.dao.FormDao;
import br.com.company.util.MessagesUtil;

@RestController
public class FormController {

	public FormDao formDao = new FormDao();
	public static final DBCollection COLLECTION = MongoConnection.getCollection();
	public static final String TITLE = "";

	// Devolve uma lista de Formulario
	@RequestMapping(value = "/templates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public List<DBObject> getTemplates() {
		return formDao.listForms();
	}

	// Devolve um Formulario com todos os campos
	@RequestMapping(value = "/templates/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public DBObject getTemplatesId(@PathVariable("id") String id) {
		return formDao.findFormById(id);
	}

	// Adiciona um Formulario
	@RequestMapping(value = "/templates", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<String> postTemplates(@RequestBody String fields) {
		if (fields != null && !fields.isEmpty()) {
			Object object = JSON.parse(fields);
			DBObject dbObject = (DBObject) object;
			JSONObject jsonObject = new JSONObject();
			if (dbObject.containsField(TITLE)) {
				if (formDao.insertForm(fields)) {
					return new ResponseEntity<String>(MessagesUtil.FORM_INSERT_SUCCESS.getMessage(), HttpStatus.OK);
				} else {
					return new ResponseEntity<String>(MessagesUtil.FORM_INSERT_FAIL.getMessage(), HttpStatus.BAD_REQUEST);
				}
			} else {
				jsonObject.put("message", MessagesUtil.NOT_NULL_TITLE.getMessage());
				jsonObject.put("category", "title");
				return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.BAD_REQUEST);
			}
		}
		return new ResponseEntity<String>(MessagesUtil.FORM_INSERT_FAIL.getMessage(),HttpStatus.BAD_REQUEST);

	}

	// Atualiza um template
	@RequestMapping(value = "/templates/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public String putFormId(@PathVariable("id") String id, @RequestBody String fields) {
		DBObject dbObject = formDao.findFormById(id);
		JSONObject jsonObject = new JSONObject();
		if (dbObject != null) {
			Object object = JSON.parse(fields);
			dbObject = (DBObject) object;
			if (formDao.updateForm(id, dbObject)) {
				jsonObject.put("message", MessagesUtil.FORM_UPDATE_SUCCESS.getMessage());
				return jsonObject.toString();
			} else {
				jsonObject.put("message", MessagesUtil.FORM_UPDATE_FAIL.getMessage());
				return jsonObject.toString();
			}
		} else {
			jsonObject.put("message", MessagesUtil.FORM_UPDATE_FAIL.getMessage());
			return jsonObject.toString();
		}

	}

	// Remove um template
	@RequestMapping(value = "/templates/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON)
	public String deleteFormId(@PathVariable("id") String id) {
		DBObject dbObject = formDao.findFormById(id);
		JSONObject jsonMessage = new JSONObject();
		if (dbObject != null) {
			if (formDao.deleteForm(dbObject)) {
				jsonMessage.put("message", MessagesUtil.FORM_DELETE_SUCCESS.getMessage());
				return jsonMessage.toString();
			} else {
				jsonMessage.put("message", MessagesUtil.FORM_DELETE_FAIL.getMessage());
				return jsonMessage.toString();
			}
		} else {
			jsonMessage.put("message", MessagesUtil.FORM_NOT_FOUND.getMessage());
			return jsonMessage.toString();
		}
	}

	// Devolve uma lista dos dados preenchidos em um formulario template
	@RequestMapping(value = "/templates/{id}/data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public DBObject getFormIdData(@PathVariable("id") String id, @PathVariable("id") String data) {
		return formDao.findFormById(id);
	}

	// Adiciona um dado num formulario
	@RequestMapping(value = "/templates/{id}/data", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON)
	public String postFormIdData(@PathVariable("id") String id, @RequestBody Map<String, Object> data) {
		DBObject dbObject = formDao.findFormById(id);
		JSONObject jsonMessage = new JSONObject();
		if (dbObject != null) {
			Set<String> map = data.keySet();
			for (String key : map) {
				dbObject.put(key, data.get(key));
			}
			if (formDao.addFieldOnForm(id, dbObject)) {
				jsonMessage.put("message", MessagesUtil.FORM_UPDATE_SUCCESS.getMessage());
				return jsonMessage.toString();
			} else {
				jsonMessage.put("message", MessagesUtil.FORM_UPDATE_FAIL.getMessage());
				return jsonMessage.toString();
			}
		} else {
			jsonMessage.put("message", MessagesUtil.FORM_UPDATE_FAIL.getMessage());
			return jsonMessage.toString();
		}
	}

}
