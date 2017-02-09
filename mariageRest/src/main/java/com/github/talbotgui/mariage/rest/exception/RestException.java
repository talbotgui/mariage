package com.github.talbotgui.mariage.rest.exception;

import com.github.talbotgui.mariage.metier.exception.BaseException;
import com.github.talbotgui.mariage.metier.exception.ExceptionId;
import com.github.talbotgui.mariage.metier.exception.ExceptionId.ExceptionLevel;

public class RestException extends BaseException {

	public static final ExceptionId ERREUR_ACTION_RESERVEE_ADMIN = new ExceptionId("ERREUR_ACTION_RESERVEE_ADMIN",
			"Cette action nécessite le role ADMINISTRATEUR", //
			ExceptionLevel.ERROR, 403);

	public static final ExceptionId ERREUR_FORMAT_DATE = new ExceptionId("ERREUR_FORMAT_DATE",
			"Le format de la date est incorrect (format='{0}', valeur='{1}').", ExceptionLevel.ERROR, 400);

	public static final ExceptionId ERREUR_FORMAT_NOMBRE = new ExceptionId("ERREUR_FORMAT_NOMBRE",
			"Le format du nombre est incorrect (valeur='{0}').", ExceptionLevel.ERROR, 400);

	public static final ExceptionId ERREUR_IO = new ExceptionId("ERREUR_IO", "Erreur d'écriture durant le traitement.",
			ExceptionLevel.ERROR, 500);

	public static final ExceptionId ERREUR_VALEUR_PARAMETRE = new ExceptionId("ERREUR_VALEUR_PARAMETRE",
			"Le parametre '{0}' ne peut prendre que les valeurs '{1}' et pas la valeur '{2}'.", //
			ExceptionLevel.ERROR, 400);

	/** Default UID. */
	private static final long serialVersionUID = 1L;

	public RestException(final ExceptionId pExceptionId) {
		super(pExceptionId);
	}

	public RestException(final ExceptionId pExceptionId, final Exception pNestedException) {
		super(pExceptionId, pNestedException);
	}

	public RestException(final ExceptionId pExceptionId, final Object... pParameters) {
		super(pExceptionId, pParameters);
	}

	public RestException(final ExceptionId pExceptionId, final Throwable pNestedException,
			final Object... pParameters) {
		super(pExceptionId, pNestedException, pParameters);
	}
}
