package com.wheroj.e3.sso.impl;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.wheroj.common.JsonUtils;
import com.wheroj.common.pojo.Result;
import com.wheroj.e3.mapper.TbUserMapper;
import com.wheroj.e3.pojo.TbUser;
import com.wheroj.e3.pojo.TbUserExample;
import com.wheroj.e3.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private TbUserMapper tbUserMapper;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Value("${TOKEN_EXPIRE}")
	private int tokenExpire;
	
	@Override
	public Result<String> login(String username, String password) {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return new Result<String>(400, "用户名或密码不能为空", null);
		}
		
		String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());
		TbUserExample example = new TbUserExample();
		example.createCriteria().andUsernameEqualTo(username)
		.andPasswordEqualTo(md5DigestAsHex);
		List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
		if (tbUsers.size() != 1) {
			return new Result<String>(400, "用户名或密码错误", null);
		}

		String token = UUID.randomUUID().toString();
		ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
		TbUser tbUser = tbUsers.get(0);
		tbUser.setPassword(null);
		opsForValue.set("SESSION:" + token, JsonUtils.objectToJson(tbUser));
		redisTemplate.expire("SESSION:" + token, tokenExpire, TimeUnit.SECONDS);
		return Result.ok(token);
	}

	@Override
	public Result<Boolean> loginOut(String token) {
		redisTemplate.expire("SESSION:" + token, 0, TimeUnit.SECONDS);
		return Result.ok(true);
	}
}
