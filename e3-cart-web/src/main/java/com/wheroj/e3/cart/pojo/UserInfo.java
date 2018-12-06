package com.wheroj.e3.cart.pojo;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wheroj.common.CookieUtils;
import com.wheroj.common.pojo.Result;
import com.wheroj.e3.pojo.TbUser;
import com.wheroj.e3.service.TokenService;

@Component
public class UserInfo {
	@Autowired
	private TokenService tokenService;
	
	public Long getUserId(HttpServletRequest request) {
		String token = CookieUtils.getCookieValue(request, "token");
		if (!StringUtils.isBlank(token)) {
			Result<TbUser> result = tokenService.getUserByToken(token);
			TbUser tbUser = result.getData();
			return tbUser.getId();
		}
		return -1L;
	}
	
	public TbUser getUser(HttpServletRequest request) {
		String token = CookieUtils.getCookieValue(request, "token");
		if (!StringUtils.isBlank(token)) {
			Result<TbUser> result = tokenService.getUserByToken(token);
			if (result.getStatus() == 200) {
				TbUser tbUser = result.getData();
				return tbUser;
			}
		}
		return null;
	}
}
