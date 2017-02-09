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
	private ExceptionId exceptionId;

	/** Message parameters. */
	private Object[] parameters;

	/**
	 * Constructor.
	 *
	 * @param exceptionId
	 *            Exception identifier.
	 */
	public BaseException(final ExceptionId exceptionId) {
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
	public BaseException(final ExceptionId pExceptionId, final Object... pParameters) {
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
	public BaseException(final ExceptionId exceptionId, final Throwable nestedException) {
		super(nestedException);
		this.setExceptionId(exceptionId);
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
	public BaseException(final ExceptionId pExceptionId, final Throwable pNested, final Object... pParameters) {
		super(pNested);
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
		if (this.exceptionId == null) {
			result = super.getMessage();
		} else {
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
	 * SETTER.
	 *
	 * @param exceptionId
	 *            Exception identifier.
	 */
	private void setExceptionId(final ExceptionId exceptionId) {
		this.exceptionId = exceptionId;
	}

	/**
	 * SETTER.
	 *
	 * @param pParameters
	 *            Message parameters.
	 */
	private void setParameters(final Object... pParameters) {
		this.parameters = Arrays.copyOf(pParameters, pParameters.length);
	}
}
