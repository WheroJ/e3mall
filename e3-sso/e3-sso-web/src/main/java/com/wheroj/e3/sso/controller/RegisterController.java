package com.wheroj.e3.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheroj.common.pojo.Result;
import com.wheroj.e3.pojo.TbUser;
import com.wheroj.e3.service.RegisterService;

@Controller
public class RegisterController {
	@Autowired
	private RegisterService registerService;
	
	@RequestMapping("/page/register")
	public String showRegister() {
		return "register";
	}
	
	@RequestMapping("/user/check/{data}/{type}")
	@ResponseBody
	public Result<Boolean> checkRegName(
			@PathVariable("data") String regName ,
			@PathVariable("type") int type) {
		return Result.ok(registerService.checkData(regName, type));
	}
	
	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> register(TbUser tbUser){
		return registerService.register(tbUser);
	}
}
