package com.github.talbotgui.mariage.metier.exception;

import java.security.InvalidParameterException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.github.talbotgui.mariage.metier.exception.ExceptionId.ExceptionLevel;

public class BaseExceptionTest {

	private static final ExceptionId ERREUR_DE_TEST = new ExceptionId("ERREUR_DE_TEST",
			"Message avec une String '{0}', un array '{1}' et une collection '{2}'", ExceptionLevel.ERROR, 400);

	private static final ExceptionId ERREUR_DE_TEST_SIMPLE = new ExceptionId("ERREUR_DE_TEST_SIMPLE", "Message",
			ExceptionLevel.ERROR, 400);

	@Test
	public void testBusinessExceptionExceptionIdArray() {

		//
		final Exception cause = new InvalidParameterException();
		final Object[] array = new String[] { "a", "b", "c", };
		final Object[] params = new Object[] { "toto", array, Arrays.asList(array) };

		//
		final BusinessException e = new BusinessException(ERREUR_DE_TEST, cause, params);

		//
		Assert.assertEquals(cause, e.getCause());
		Assert.assertEquals(Arrays.asList(params), Arrays.asList(e.getParameters()));
		Assert.assertEquals(ERREUR_DE_TEST, e.getExceptionId());
	}

	@Test
	public void testEqualsExceptionExceptionIdKo() {
		//
		final BusinessException e = new BusinessException(ERREUR_DE_TEST);

		//
		Assert.assertFalse(BaseException.equals(e, ERREUR_DE_TEST_SIMPLE));
	}

	@Test
	public void testEqualsExceptionExceptionIdOk() {
		//
		final BusinessException e = new BusinessException(ERREUR_DE_TEST);

		//
		Assert.assertTrue(BaseException.equals(e, ERREUR_DE_TEST));
	}

	@Test
	public void testGetMessage() {
		//
		final Exception cause = new InvalidParameterException();
		final Object[] array = new String[] { "a", "b", "c", };
		final Object[] params = new Object[] { "toto", array, Arrays.asList(array) };

		//
		final BusinessException e = new BusinessException(ERREUR_DE_TEST, cause, params);

		//
		Assert.assertEquals("Message avec une String 'toto', un array '[a, b, c]' et une collection '[a, b, c]'",
				e.getMessage());
	}

}
