package com.wheroj.e3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wheroj.common.pojo.Result;
import com.wheroj.e3.ErrorCode;
import com.wheroj.e3.pojo.PageVo;
import com.wheroj.e3.pojo.TbItem;
import com.wheroj.e3.pojo.TbItemDesc;
import com.wheroj.e3.service.TbItemService;

@RestController
public class TbItemController {
	
	@Autowired
	private TbItemService tbItemService;
	
	@GetMapping(value = "/item/{id}")
	public TbItem getItem(@PathVariable("id") Long id) {
		return tbItemService.getItemById(id);
	}
	
	@GetMapping("/item/list")
	public PageVo<TbItem> getItemsByPage(
			@RequestParam(value = "page", required = true) int page, 
			@RequestParam(value = "rows", required = true) int rows) {
		return tbItemService.getItemsByPage(page, rows);
	}
	
	@PostMapping("/item/save")
	public Result<TbItem> insertItem(TbItem item, String desc) {
		TbItem insertItem = tbItemService.insertItem(item, desc);
		return Result.ok(insertItem);
	}
	
	@GetMapping("/rest/page/item-edit")
	public Result<TbItem> modifyItem(Long itemId) {
		return Result.ok(tbItemService.getItemById(itemId));
	}
	
	@GetMapping("/rest/item/query/item/desc/{id}")
	public Result<TbItemDesc> selectItemDesc(@PathVariable("id") Long id) {
		TbItemDesc itemDesc = tbItemService.getItemDesc(id);
		return Result.ok(itemDesc);
	}
	
	@PostMapping("/rest/item/delete")
	public Result<?> deleteItems(Long[] ids) {
		int deleteItems = tbItemService.deleteItems(ids);
		return Result.ok(deleteItems);
	}

	@PostMapping("/rest/item/instock")
	public Result<?> instockItems(Long[] ids) {
		int count = tbItemService.instockItems(ids);
		if (count == ids.length) {
			return Result.ok(count);
		} else {
			return Result.error(ErrorCode.ERROR_INSTOCK, "商品未全部下架");
		}
	}
	
	@PostMapping("/rest/item/reshelf")
	public Result<?> reshelfItems(Long[] ids) {
		int count = tbItemService.reshelfItems(ids);
		if (count == ids.length) {
			return Result.ok(count);
		} else {
			return Result.error(ErrorCode.ERROR_RESHELF, "商品未全部上架");
		}
	}
	
	@PostMapping("/rest/item/update")
	public Result<?> updateItem(TbItem tbItem, String desc, String itemParams) {
		tbItemService.updateItem(tbItem, desc, itemParams);
		return Result.ok(null);
	}
}