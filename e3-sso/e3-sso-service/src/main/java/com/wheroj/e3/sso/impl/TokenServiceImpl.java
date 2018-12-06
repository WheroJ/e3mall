package com.wheroj.e3.sso.impl;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.wheroj.common.JsonUtils;
import com.wheroj.common.pojo.Result;
import com.wheroj.e3.pojo.TbUser;
import com.wheroj.e3.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Value("${TOKEN_EXPIRE}")
	private int tokenExpire;
	
	@Override
	public Result<TbUser> getUserByToken(String token) {
		ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
		String json = (String)opsForValue.get("SESSION:" + token);
		if (StringUtils.isBlank(json)) {
			return new Result<TbUser>(201, "用户信息已过期", null);
		}
		redisTemplate.expire("SESSION:" + token, tokenExpire, TimeUnit.SECONDS);
		return Result.ok(JsonUtils.jsonToObject(json, TbUser.class));
	}
}
