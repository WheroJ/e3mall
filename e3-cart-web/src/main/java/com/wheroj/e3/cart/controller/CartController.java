package com.wheroj.e3.cart.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wheroj.common.CookieUtils;
import com.wheroj.common.JsonUtils;
import com.wheroj.common.pojo.Result;
import com.wheroj.e3.cart.CartService;
import com.wheroj.e3.pojo.TbItem;
import com.wheroj.e3.pojo.TbUser;
import com.wheroj.e3.service.TbItemService;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private TbItemService tbItemService;
	
	@Value("${COOKIE_CART_EXPIRE}")
	private int COOKIE_CART_EXPIRE;
	
	@RequestMapping("/cart")
	public String showCart(Model model, HttpServletRequest request, HttpServletResponse response) {
		TbUser user = (TbUser) request.getAttribute("user");
		List<TbItem> cartList = getCookieCartList(request);
		if (user != null) {
			if (!cartList.isEmpty()) {
				cartService.mergeCart(user.getId() + "", cartList);
				cartList.clear();
				CookieUtils.deleteCookie(request, response, "cart");
			}
			cartList = cartService.getCartList(user.getId() + "");
		}
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	
	@RequestMapping("/add/{itemId}")
	public String addCart (
			@PathVariable("itemId") long itemId,
			@RequestParam(value="num", required=true) int num,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		TbUser tbUser = (TbUser) request.getAttribute("user");
		if (tbUser != null) {
			cartService.addCart(tbUser.getId()+"", itemId, num);
		} else {
			List<TbItem> cookieCartList = getCookieCartList(request);
			
			boolean flag = false;
			for (int i = 0; i < cookieCartList.size(); i++) {
				TbItem tbItem = cookieCartList.get(i);
				if (tbItem.getId().equals(itemId)) {
					flag = true;
					tbItem.setNum(tbItem.getNum() + num);
					break;
				}
			}
			
			if (!flag) {
				TbItem tbItem = tbItemService.getItemById(itemId);
				tbItem.setNum(num);
				cookieCartList.add(tbItem);
			}
			
			CookieUtils.setCookie(request, response, "cart", JsonUtils.listToJson(cookieCartList), COOKIE_CART_EXPIRE, true);
		}
		return "cartSuccess";
	}
	
	@RequestMapping("/delete/{itemId}")
	@ResponseBody
	public void removeProduct(@PathVariable("itemId") long itemId, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		TbUser user = (TbUser) request.getAttribute("user");
		if (user == null) {
			List<TbItem> cookieCartList = getCookieCartList(request);
			
			int index = -1;
			for (int i = 0; i < cookieCartList.size(); i++) {
				if (cookieCartList.get(i).getId() == itemId) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				cookieCartList.remove(index);
				CookieUtils.setCookie(request, response, "cart", JsonUtils.listToJson(cookieCartList), COOKIE_CART_EXPIRE, true);
			}
		} else {
			cartService.removeProduct(user.getId() + "", itemId);
		}
		response.sendRedirect("/cart/cart.html");
	}
	
	@RequestMapping(value="/update/num/{itemId}/{num}", method = RequestMethod.POST)
	public @ResponseBody Result<?> updateNum(
			@PathVariable("itemId") Long itemId,
			@PathVariable("num") Integer num, 
			HttpServletRequest request, HttpServletResponse response) {
		TbUser user = (TbUser) request.getAttribute("user");
		if (user == null) {
			List<TbItem> cookieCartList = getCookieCartList(request);
			
			int index = -1;
			for (int i = 0; i < cookieCartList.size(); i++) {
				if (cookieCartList.get(i).getId().equals(itemId)) {
					index = i;
					break;
				}
			}
			
			if (index != -1) {
				cookieCartList.get(index).setNum(num);
				CookieUtils.setCookie(request, response, "cart", JsonUtils.listToJson(cookieCartList), COOKIE_CART_EXPIRE, true);
			}
		} else {
			cartService.updateProduct(user.getId() + "", itemId, num);
		}
		return Result.ok(null);
	}
	
	private List<TbItem>  getCookieCartList(HttpServletRequest request) {
		List<TbItem> cartList = new ArrayList<TbItem>();
		
		String cookieValue = CookieUtils.getCookieValue(request, "cart", true);
		if (!StringUtils.isBlank(cookieValue)) {
			cartList = JsonUtils.jsonToObject(cookieValue, new TypeReference<ArrayList<TbItem>>() {
			});
		}
		return cartList;
	}
}
