package com.wheroj.e3.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheroj.common.CookieUtils;
import com.wheroj.common.pojo.Result;
import com.wheroj.e3.service.LoginService;

@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	@RequestMapping("/page/login")
	public String showLogin(String redirect, HttpServletRequest request) {
		request.setAttribute("redirect", redirect);
		return "login";
	}
	
	@RequestMapping(value="/user/login", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> login(HttpServletRequest request, HttpServletResponse response, 
			String username, String password) {
		Result<String> result = loginService.login(username, password);
		if (result.getStatus() == 200) {
			CookieUtils.setCookie(request, response, "token", result.getData());
		}
		return result;
	}
	
	@RequestMapping(value="/user/logout/{token}")
	public String loginOut(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable("token") String token) {
		CookieUtils.setCookie(request, response, "token", token, 1);
		loginService.loginOut(token);
		return "redirect:/page/login";
	}
}
