package com.github.talbotgui.mariage.metier.exception;

import com.github.talbotgui.mariage.metier.exception.ExceptionId.ExceptionLevel;

public class BusinessException extends BaseException {

	public static final ExceptionId ERREUR_ID_MARIAGE = new ExceptionId("ERREUR_ID_MARIAGE",
			"Identifiant de mariage invalide (valeur={0})", ExceptionLevel.ERROR, 400);

	/** Default UID. */
	private static final long serialVersionUID = 1L;

	public BusinessException(final ExceptionId exceptionId) {
		super(exceptionId);
	}

	public BusinessException(final ExceptionId pExceptionId, final Object[] pParameters) {
		super(pExceptionId, pParameters);
	}

	public BusinessException(final ExceptionId exceptionId, final Throwable nestedException) {
		super(exceptionId, nestedException);
	}

	public BusinessException(final ExceptionId pExceptionId, final Throwable pNestedException,
			final Object[] pParameters) {
		super(pExceptionId, pNestedException, pParameters);
	}

}
