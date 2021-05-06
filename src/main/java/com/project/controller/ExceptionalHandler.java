package com.project.controller;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.project.model.ApiError;
import com.project.service.ApiException;

public class ExceptionalHandler {

	@ExceptionHandler(ApiException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiError HandleNoSuchElementException(ApiException exception,HttpServletRequest request) {
		ApiError error= new ApiError(404,exception.getMessage(),request.getServletPath());
		return error;
	}


	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiError HandleSuchElementException(ConstraintViolationException exception,HttpServletRequest request) {
		ApiError error= new ApiError(404,"Duplicate ntry",request.getServletPath());
		return error;
	}
	
}
