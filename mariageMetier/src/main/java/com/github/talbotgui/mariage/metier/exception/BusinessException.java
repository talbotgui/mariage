package com.github.talbotgui.mariage.metier.exception;

import com.github.talbotgui.mariage.metier.exception.ExceptionId.ExceptionLevel;

public class BusinessException extends BaseException {

	public static final ExceptionId ERREUR_COURRIER_LIE_A_ETAPE = new ExceptionId("ERREUR_COURRIER_LIE_A_ETAPE",
			"Impossible de supprimer l'étape car un courrier y est lié", ExceptionLevel.ERROR, 400);

	public static final ExceptionId ERREUR_ID_MARIAGE = new ExceptionId("ERREUR_ID_MARIAGE",
			"Identifiant de mariage invalide (valeur={0})", ExceptionLevel.ERROR, 400);

	public static final ExceptionId ERREUR_LOGIN = new ExceptionId("ERREUR_LOGIN", "Erreur de connexion",
			ExceptionLevel.ERROR, 403);

	public static final ExceptionId ERREUR_LOGIN_MDP = new ExceptionId("ERREUR_LOGIN_MDP",
			"Identifiant et/ou mot de passe trop court ({0} caractères minimum)", ExceptionLevel.ERROR, 400);

	public static final ExceptionId ERREUR_LOGIN_VEROUILLE = new ExceptionId("ERREUR_LOGIN_VEROUILLE",
			"Erreur de connexion - le compte est verrouillé", ExceptionLevel.ERROR, 403);

	public static final ExceptionId ERREUR_SHA = new ExceptionId("ERREUR_SHA", "Erreur de cryptage",
			ExceptionLevel.ERROR, 500);

	public static final ExceptionId ERREUR_TRANSFORMATION_MODEL = new ExceptionId("ERREUR_TRANSFORMATION_MODEL",
			"Erreur durant la tranformation de modele.", ExceptionLevel.ERROR, 500);

	/** Default UID. */
	private static final long serialVersionUID = 1L;

	public BusinessException(final ExceptionId exceptionId) {
		super(exceptionId);
	}

	public BusinessException(final ExceptionId pExceptionId, final Object... pParameters) {
		super(pExceptionId, pParameters);
	}

	public BusinessException(final ExceptionId exceptionId, final Throwable nestedException) {
		super(exceptionId, nestedException);
	}

	public BusinessException(final ExceptionId pExceptionId, final Throwable pNestedException,
			final Object... pParameters) {
		super(pExceptionId, pNestedException, pParameters);
	}

}
