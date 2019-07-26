package com.persistance.DomainModel;

import java.util.Locale;

import org.springframework.context.MessageSource;

public class GenericResponse {

	private boolean error;
	private String message;
	private String messageCode;

	public GenericResponse(MessageSource messageSource, String messageCode,
			boolean error) {

		this.error = error;
		this.messageCode = messageCode;
		this.message = messageSource.getMessage(messageCode, null,
				Locale.getDefault());
	}

	public GenericResponse(MessageSource messageSource, String messageCode) {
		this(messageSource, messageCode, false);
	}

	public GenericResponse(MessageSource messageSource) {
		this(messageSource, "success");
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
}
