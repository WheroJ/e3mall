package com.wheroj.e3.cart.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wheroj.common.pojo.Result;
import com.wheroj.e3.cart.CartService;
import com.wheroj.e3.pojo.TbUser;
import com.wheroj.e3.service.TokenService;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private TokenService tokenService;
	
	@RequestMapping("/add/{itemId}")
	public ModelAndView addCart(
			@PathVariable("itemId") long itemId,
			@RequestParam(value="num", required=true) int num,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView();

		String token = (String) request.getSession().getAttribute("token");
		if (!StringUtils.isBlank(token)) {
			Result<TbUser> result = tokenService.getUserByToken(token);
			TbUser tbUser = result.getData();
			if (tbUser != null) {
				cartService.addCart(tbUser.getId()+"", itemId, num);
				modelAndView.setViewName("cartSuccess");
			} else {
				try {
					response.sendRedirect("http://localhost:8095/page/login");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			response.sendRedirect("http://localhost:8095/page/login");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return modelAndView;
	}
}
