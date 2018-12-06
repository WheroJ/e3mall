package com.wheroj.e3.cart.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wheroj.common.pojo.Result;
import com.wheroj.e3.cart.CartService;
import com.wheroj.e3.pojo.TbItem;
import com.wheroj.e3.pojo.TbUser;
import com.wheroj.e3.service.TokenService;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private TokenService tokenService;
	
	@RequestMapping("/cart")
	public String showCart(Model model, HttpServletRequest request) {
		TbUser user = (TbUser) request.getAttribute("user");
		List<TbItem> cartList = cartService.getCartList(user.getId() + "");
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	
	@RequestMapping("/add/{itemId}")
	public ModelAndView addCart (
			@PathVariable("itemId") long itemId,
			@RequestParam(value="num", required=true) int num,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModelAndView modelAndView = null;

		String token = null;
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals("token")) {
				token = cookies[i].getValue();
			}
		}
		
		if (!StringUtils.isBlank(token)) {
			Result<TbUser> result = tokenService.getUserByToken(token);
			TbUser tbUser = result.getData();
			if (tbUser != null) {
				cartService.addCart(tbUser.getId()+"", itemId, num);
				modelAndView = new ModelAndView();
				modelAndView.setViewName("cartSuccess");
			} else {
				response.sendRedirect("http://localhost:8095/page/login");
			}
		} else {
			response.sendRedirect("http://localhost:8095/page/login");
		}
		
		return modelAndView;
	}
	
	@RequestMapping("/delete/{itemId}")
	@ResponseBody
	public void removeProduct(@PathVariable("itemId") long itemId, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		TbUser user = (TbUser) request.getAttribute("user");
		cartService.removeProduct(user.getId() + "", itemId);
		response.sendRedirect("/cart/cart.html");
	}
	
	@RequestMapping(value="/update/num/{itemId}/{num}", method = RequestMethod.POST)
	public @ResponseBody Result<Long> updateNum(
			@PathVariable("itemId") Long itemId,
			@PathVariable("num") Integer num, 
			HttpServletRequest request, HttpServletResponse response) {
		TbUser user = (TbUser) request.getAttribute("user");
		Long updateProduct = cartService.updateProduct(user.getId() + "", itemId, num);
		return Result.ok(updateProduct);
	}
}
