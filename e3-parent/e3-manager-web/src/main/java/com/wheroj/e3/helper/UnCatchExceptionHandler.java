package com.wheroj.e3.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UnCatchExceptionHandler {
	
	private static Logger logger = LoggerFactory.getLogger(UnCatchExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public String handleException(Exception e) {
		logger.error(e.getMessage(), e);
		return "出现异常";
	}
}
