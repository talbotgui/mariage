package com.github.talbotgui.mariage.rest.exception;

import com.github.talbotgui.mariage.metier.exception.BaseException;
import com.github.talbotgui.mariage.metier.exception.ExceptionId;
import com.github.talbotgui.mariage.metier.exception.ExceptionId.ExceptionLevel;

/**
 */
public class RestException extends BaseException {

	public static final ExceptionId ERREUR_FORMAT_DATE = new ExceptionId("ERREUR_FORMAT_DATE",
			"Le format de la date est incorrecte (format='{0}', valeur='{1}').", ExceptionLevel.ERROR, 400);

	public static final ExceptionId ERREUR_VALEUR_PARAMETRE = new ExceptionId("ERREUR_VALEUR_PARAMETRE",
			"Le parametre '{1}' ne peut prendre que les valeurs '{2}' et pas la valeur '{3}'.", ExceptionLevel.ERROR,
			400);
	/** Default UID. */
	private static final long serialVersionUID = 1L;

	public RestException(ExceptionId exceptionId) {
		super(exceptionId);
	}

	public RestException(ExceptionId pExceptionId, Object[] pParameters) {
		super(pExceptionId, pParameters);
	}

	public RestException(ExceptionId exceptionId, Throwable nestedException) {
		super(exceptionId, nestedException);
	}

	public RestException(ExceptionId pExceptionId, Throwable pNestedException, String[] pParameters) {
		super(pExceptionId, pNestedException, pParameters);
	}
}
