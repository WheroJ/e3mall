package com.wheroj.e3.sso.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.wheroj.common.pojo.Result;
import com.wheroj.e3.mapper.TbUserMapper;
import com.wheroj.e3.pojo.TbUser;
import com.wheroj.e3.pojo.TbUserExample;
import com.wheroj.e3.pojo.TbUserExample.Criteria;
import com.wheroj.e3.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private TbUserMapper tbUserMapper;
	
	@Override
	public Boolean checkData(String data, int type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		switch (type) {
		case 1:
			criteria.andUsernameEqualTo(data);
			break;
		case 2:
			criteria.andPhoneEqualTo(data);
			break;
		case 3:
			criteria.andEmailEqualTo(data);
			break;
		default:
			return false;
		}
		return tbUserMapper.countByExample(example) == 0;
	}

	@Override
	public Result<Boolean> register(TbUser tbUser) {
		if (StringUtils.isBlank(tbUser.getUsername()) 
				|| StringUtils.isBlank(tbUser.getPhone())) {
			return new Result<Boolean>(400, "用户名和手机号不能为空", false);
		}
		
		if (!checkData(tbUser.getUsername(), 1)) {
			return new Result<Boolean>(400, "用户名已经存在", false);
		}
		
		if (!checkData(tbUser.getPhone(), 2)) {
			return new Result<Boolean>(400, "手机号已经存在", false);
		}
		
		String md5DigestAsHex = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
		tbUser.setPassword(md5DigestAsHex);
		
		tbUserMapper.insertSelective(tbUser);
		return Result.ok(true);
	}
}
