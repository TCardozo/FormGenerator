package br.com.company.util;

public enum MessagesUtil {
	
	FORM_DELETE_SUCCESS("Form successfully removed"), 
	FORM_DELETE_FAIL("Form was not removed"), 
	FORM_NOT_FOUND("Form not found"), 
	FORM_UPDATE_SUCCESS("Form Updated Successfully"), 
	FORM_UPDATE_FAIL("Form was not updated"), 
	FORM_INSERT_SUCCESS("Form included successfully"), 
	FORM_INSERT_FAIL("Form was not included"),
	
	NOT_NULL_TITLE("The title can not be null"),
	NO_CONTENT("No content");

	private final String message;

	MessagesUtil(String msg) {
		message = msg;
	}

	public String getMessage() {
		return message;
	}
}
