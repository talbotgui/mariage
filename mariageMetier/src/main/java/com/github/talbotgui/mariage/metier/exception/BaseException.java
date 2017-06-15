package com.github.talbotgui.mariage.metier.exception;

import java.util.Arrays;

/**
 * 400 : BAD REQUEST - La syntaxe de la requête est erronée.
 *
 * <p>
 * 409 : CONFLICT - La requête ne peut être traitée à l’état actuel
 * </p>
 */
public abstract class BaseException extends RuntimeException {

	/** Default UID. */
	private static final long serialVersionUID = 1L;

	public static boolean equals(final Exception e, final ExceptionId id) {
		if (!BaseException.class.isInstance(e)) {
			return false;
		}
		return ((BaseException) e).getExceptionId().equals(id);
	}

	/** Exception identifier. */
	private final ExceptionId exceptionId;

	/** Message parameters. */
	private final String[] parameters;

	/**
	 * Constructor.
	 *
	 * @param exceptionId
	 *            Exception identifier.
	 */
	public BaseException(final ExceptionId exceptionId) {
		super();
		this.exceptionId = exceptionId;
		this.parameters = null;
	}

	/**
	 * Constructor.
	 *
	 * @param pExceptionId
	 *            Exception identifier.
	 * @param pParameters
	 *            Message parameters.
	 */
	public BaseException(final ExceptionId pExceptionId, final String... pParameters) {
		this.exceptionId = pExceptionId;
		this.parameters = pParameters;
	}

	/**
	 * Constructor.
	 *
	 * @param exceptionId
	 *            Exception identifier.
	 * @param nestedException
	 *            Embedded exception.
	 */
	public BaseException(final ExceptionId exceptionId, final Throwable nestedException) {
		super(nestedException);
		this.exceptionId = exceptionId;
		this.parameters = null;
	}

	/**
	 * Constructor.
	 *
	 * @param pExceptionId
	 *            Exception identifier.
	 * @param pNested
	 *            Embedded exception.
	 * @param pParameters
	 *            Message parameters.
	 */
	public BaseException(final ExceptionId pExceptionId, final Throwable pNested, final String... pParameters) {
		super(pNested);
		this.exceptionId = pExceptionId;
		this.parameters = pParameters;
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
		if (this.exceptionId == null) {
			result = super.getMessage();
		} else {
			String message = this.exceptionId.getDefaultMessage();
			if (message != null && this.parameters != null) {
				for (int i = 0; i < this.parameters.length; i++) {
					final String valeur = this.transformParameterToString(i);
					message = message.replace("{" + i + "}", valeur);
				}
			}
			result = message;
		}
		return result;
	}

	/**
	 * GETTER.
	 *
	 * @return Message parameters.
	 */
	public Object[] getParameters() {
		if (this.parameters != null) {
			return Arrays.copyOf(this.parameters, this.parameters.length);
		}
		return new Object[0];
	}

	/**
	 * Transforme le parameter[i] en string
	 *
	 * @param i
	 *            L'index du paramètre
	 * @return la String
	 */
	private String transformParameterToString(final int i) {
		String valeur = "null";
		if (this.parameters[i] != null) {
			if (this.parameters[i].getClass().isArray()) {
				valeur = Arrays.asList(this.parameters[i]).toString();
			} else {
				valeur = this.parameters[i].toString();
			}
		}
		return valeur;
	}

}
