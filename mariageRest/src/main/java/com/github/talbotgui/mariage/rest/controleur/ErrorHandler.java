package com.github.talbotgui.mariage.rest.controleur;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.talbotgui.mariage.metier.exception.BaseException;
import com.github.talbotgui.mariage.metier.exception.BusinessException;
import com.github.talbotgui.mariage.rest.exception.RestException;

@ControllerAdvice
public class ErrorHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ErrorHandler.class);

	@ResponseBody
	@ExceptionHandler({ BusinessException.class, RestException.class })
	public ResponseEntity<Object> defaultErrorHandler(final HttpServletRequest req, final BaseException e) {
		LOG.error("Erreur traitée sur la requête {}", req.getRequestURI(), e);
		return new ResponseEntity<Object>(e.getMessage(), HttpStatus.valueOf(e.getExceptionId().getHttpStatusCode()));
	}
}
