package com.wheroj.e3.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wheroj.e3.cart.pojo.UserInfo;
import com.wheroj.e3.pojo.TbUser;

public class LoginInterceptor implements HandlerInterceptor {
	@Autowired
	private UserInfo userInfo;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (request.getAttribute("user") == null) {
			TbUser user = userInfo.getUser(request);
			if (user == null) {
				//未登陆
				if (!(request.getRequestURI().endsWith("/page/login")
						|| request.getRequestURI().endsWith("/user/login"))) {
					response.sendRedirect("http://localhost:8095/page/login");
					return false;
				}
			} else {
				request.setAttribute("user", user);
			}
		}
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
