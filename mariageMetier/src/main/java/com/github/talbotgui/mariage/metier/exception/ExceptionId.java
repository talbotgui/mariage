package com.github.talbotgui.mariage.metier.exception;

import java.io.Serializable;

public class ExceptionId implements Serializable {

	public enum ExceptionLevel {
		ERROR, FATAL, INFORMATION, WARNING
	}

	private static final long serialVersionUID = 1L;

	private String defaultMessage;

	private int httpStatusCode;

	private String id;

	private ExceptionLevel level;

	public ExceptionId(final String id, final String defaultMessage, final ExceptionLevel level,
			final int httpStatusCode) {
		super();
		this.setDefaultMessage(defaultMessage);
		this.setId(id);
		this.setLevel(level);
		this.setHttpStatusCode(httpStatusCode);
	}

	@Override
	public boolean equals(final Object obj) {
		return obj != null && ExceptionId.class.isInstance(obj) && obj.hashCode() == this.hashCode();
	}

	public String getDefaultMessage() {
		return this.defaultMessage;
	}

	public int getHttpStatusCode() {
		return this.httpStatusCode;
	}

	public String getId() {
		return this.id;
	}

	public ExceptionLevel getLevel() {
		return this.level;
	}

	@Override
	public int hashCode() {
		if (this.id == null) {
			return super.hashCode();
		} else {
			return this.id.hashCode();
		}
	}

	private void setDefaultMessage(final String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	private void setHttpStatusCode(final int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	private void setId(final String id) {
		this.id = id;
	}

	private void setLevel(final ExceptionLevel type) {
		this.level = type;
	}

}
