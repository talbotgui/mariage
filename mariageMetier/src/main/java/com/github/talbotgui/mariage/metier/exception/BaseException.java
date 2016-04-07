package com.github.talbotgui.mariage.metier.exception;

import java.util.Arrays;

/**
 *
 * 400 : BAD REQUEST - La syntaxe de la requête est erronée
 *
 * 409 : CONFLICT - La requête ne peut être traitée à l’état actuel
 */
public abstract class BaseException extends RuntimeException {

	/** Default UID. */
	private static final long serialVersionUID = 1L;

	public static boolean equals(Exception e, ExceptionId id) {
		if (!BaseException.class.isInstance(e)) {
			return false;
		}
		return ((BaseException) e).getExceptionId().equals(id);
	}

	/** Exception identifier. */
	private ExceptionId exceptionId;

	/** Message parameters. */
	private Object[] parameters;

	/**
	 * Constructor.
	 *
	 * @param exceptionId
	 *            Exception identifier.
	 */
	public BaseException(ExceptionId exceptionId) {
		super();
		this.setExceptionId(exceptionId);
	}

	/**
	 * Constructor.
	 *
	 * @param pExceptionId
	 *            Exception identifier.
	 * @param pParameters
	 *            Message parameters.
	 */
	public BaseException(ExceptionId pExceptionId, Object[] pParameters) {
		this(pExceptionId);
		this.setParameters(pParameters);
	}

	/**
	 * Constructor.
	 *
	 * @param exceptionId
	 *            Exception identifier.
	 * @param nestedException
	 *            Embedded exception.
	 */
	public BaseException(ExceptionId exceptionId, Throwable nestedException) {
		super(nestedException);
		this.setExceptionId(exceptionId);
	}

	/**
	 * Constructor.
	 *
	 * @param pExceptionId
	 *            Exception identifier.
	 * @param pNestedException
	 *            Embedded exception.
	 * @param pParameters
	 *            Message parameters.
	 */
	public BaseException(ExceptionId pExceptionId, Throwable pNestedException, Object[] pParameters) {
		super(pNestedException);
		this.setExceptionId(pExceptionId);
		this.setParameters(pParameters);
	}

	/**
	 * GETTER.
	 *
	 * @return Exception identifier.
	 */
	public ExceptionId getExceptionId() {
		return this.exceptionId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		String result;
		if (this.exceptionId != null) {
			String message = this.exceptionId.getDefaultMessage();
			if (message != null && this.parameters != null) {
				for (int i = 0; i < this.parameters.length; i++) {
					String valeur = "null";
					if (this.parameters[i] != null) {
						if (this.parameters[i].getClass().isArray()) {
							valeur = Arrays.asList((Object[]) this.parameters[i]).toString();
						} else {
							valeur = this.parameters[i].toString();
						}
					}
					message = message.replace("{" + i + "}", valeur);
				}
			}
			result = message;
		} else {
			result = super.getMessage();
		}
		return result;
	}

	/**
	 * GETTER.
	 *
	 * @return Message parameters.
	 */
	public Object[] getParameters() {
		return this.parameters;
	}

	/**
	 * SETTER.
	 *
	 * @param exceptionId
	 *            Exception identifier.
	 */
	private void setExceptionId(ExceptionId exceptionId) {
		this.exceptionId = exceptionId;
	}

	/**
	 * SETTER.
	 *
	 * @param pParameters
	 *            Message parameters.
	 */
	public void setParameters(Object[] pParameters) {
		this.parameters = pParameters;
	}
}
