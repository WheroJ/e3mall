package com.wheroj.e3.service;

import com.wheroj.common.pojo.Result;

public interface LoginService {
	public Result<String> login(String username, String password);
	
	public Result<Boolean> loginOut(String token);
}
