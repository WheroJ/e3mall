package com.wheroj.e3.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalUncatchExceptionHandler implements HandlerExceptionResolver{
	
	Logger Logger = LoggerFactory.getLogger(GlobalUncatchExceptionHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		Logger.error("报错啦", ex);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/error/exception");
		return modelAndView;
	}
}
