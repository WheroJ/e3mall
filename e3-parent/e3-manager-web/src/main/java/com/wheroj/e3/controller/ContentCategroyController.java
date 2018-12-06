package com.wheroj.e3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wheroj.common.pojo.Result;
import com.wheroj.content.service.TbContentCategoryService;
import com.wheroj.e3.ErrorCode;
import com.wheroj.e3.pojo.EasyUITreeNode;
import com.wheroj.e3.pojo.TbContentCategory;

@RestController
@RequestMapping("/content/category")
public class ContentCategroyController {
	@Autowired
	private TbContentCategoryService categoryService;
	
	@GetMapping("/list")
	public List<EasyUITreeNode> getCategoryNodes(
			@RequestParam(value="id", defaultValue = "0") long parentId) {
		return categoryService.getCategoryNodes(parentId);
	}
	
	@PostMapping("/create")
	public Result<TbContentCategory> insert(long parentId, String name) {
		TbContentCategory category = categoryService.insert(parentId, name);
		return Result.ok(category);
	}
	
	@PostMapping("/update")
	public Result<TbContentCategory> update(long id, String name) {
		TbContentCategory category = categoryService.update(id, name);
		return Result.ok(category);
	}
	
	@PostMapping("/delete")
	public Result<?> delete(long id) {
		int count = categoryService.delete(id);
		if (count == 1) {
			return Result.ok(count);
		} else {
			return Result.error(ErrorCode.ERROR_DELETE_CATEGORY, "删除分类失败");
		}
	}
}
