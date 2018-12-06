package com.wheroj.e3.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wheroj.e3.item.pojo.Item;
import com.wheroj.e3.pojo.TbItem;
import com.wheroj.e3.pojo.TbItemDesc;
import com.wheroj.e3.service.TbItemService;

@Controller
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private TbItemService tbItemService;
	
	@GetMapping("/{itemId}")
	public ModelAndView getDetail(@PathVariable("itemId") long itemId, ModelAndView modelAndView) {
		TbItem tbItem = tbItemService.getItemById(itemId);
		Item item = new Item(tbItem);
		TbItemDesc itemDesc = tbItemService.getItemDesc(itemId);
		modelAndView.addObject("item", item);
		modelAndView.addObject("itemDesc", itemDesc);
		modelAndView.setViewName("item");
		return modelAndView;
	}
}
