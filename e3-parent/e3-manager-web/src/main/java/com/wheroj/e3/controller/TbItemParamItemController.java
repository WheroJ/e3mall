package com.wheroj.e3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.wheroj.common.pojo.Result;
import com.wheroj.e3.pojo.TbItemParamItem;
import com.wheroj.e3.service.TbItemParamItemService;

@RestController
public class TbItemParamItemController {

	@Autowired
	private TbItemParamItemService paramItemService;
	
	@GetMapping("/rest/item/param/item/query/{id}")
	public Result<TbItemParamItem> selectParamItem(@PathVariable("id") long itemId) {
		TbItemParamItem paramItem = paramItemService.selectParamItem(itemId);
		return Result.ok(paramItem);
	}
}
