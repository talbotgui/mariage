package com.github.talbotgui.mariage.rest.controleur;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.talbotgui.mariage.metier.exception.BusinessException;

@ControllerAdvice
public class ErrorHandler {

	@ResponseBody
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Object> defaultErrorHandler(final HttpServletRequest req, final BusinessException e) {
		return new ResponseEntity<Object>(e.getExceptionId().getId() + "-" + e.getMessage(),
				HttpStatus.valueOf(e.getExceptionId().getHttpStatusCode()));
	}
}
