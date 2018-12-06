package com.wheroj.e3.service;

import com.wheroj.common.pojo.Result;
import com.wheroj.e3.pojo.TbUser;

public interface RegisterService {
	public Boolean checkData(String data, int type);
	
	public Result<Boolean> register(TbUser tbUser);
}
