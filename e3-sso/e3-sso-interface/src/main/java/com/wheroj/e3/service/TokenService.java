package com.wheroj.e3.service;

import com.wheroj.common.pojo.Result;
import com.wheroj.e3.pojo.TbUser;

public interface TokenService {
	public Result<TbUser> getUserByToken(String token);
}
