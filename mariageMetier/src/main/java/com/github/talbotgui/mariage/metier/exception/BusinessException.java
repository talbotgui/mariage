package com.github.talbotgui.mariage.metier.exception;

import com.github.talbotgui.mariage.metier.exception.ExceptionId.ExceptionLevel;

/**
 */
public class BusinessException extends BaseException {

	public static final ExceptionId ERREUR_ID_MARIAGE = new ExceptionId("ERREUR_ID_MARIAGE",
			"Identifiant de mariage invalide (valeur={0})", ExceptionLevel.ERROR, 400);

	/** Default UID. */
	private static final long serialVersionUID = 1L;

	public BusinessException(ExceptionId exceptionId) {
		super(exceptionId);
	}

	public BusinessException(ExceptionId pExceptionId, Object[] pParameters) {
		super(pExceptionId, pParameters);
	}

	public BusinessException(ExceptionId exceptionId, Throwable nestedException) {
		super(exceptionId, nestedException);
	}

	public BusinessException(ExceptionId pExceptionId, Throwable pNestedException, String[] pParameters) {
		super(pExceptionId, pNestedException, pParameters);
	}

}
