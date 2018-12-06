package com.wheroj.e3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wheroj.common.pojo.Result;
import com.wheroj.content.service.TbContentService;
import com.wheroj.e3.pojo.PageVo;
import com.wheroj.e3.pojo.TbContent;

@RestController
@RequestMapping("/content")
public class TbContentController {

	@Autowired
	private TbContentService tbContentService;
	
	@GetMapping("/query/list")
	public PageVo<TbContent> queryByPage(
			@RequestParam("categoryId") long categoryId,
			@RequestParam("page") int page,
			@RequestParam("rows") int rows) {
		return tbContentService.queryByPage(categoryId, page, rows);
	}
	
	@PostMapping("/save")
	public Result<TbContent> save(TbContent tbContent) {
		TbContent save = tbContentService.save(tbContent);
		return Result.ok(save);
	}
	
	@PostMapping("/edit")
	public Result<Integer> update(TbContent tbContent) {
		int update = tbContentService.update(tbContent);
		return Result.ok(update); 
	}
	
	@PostMapping("/delete")
	public Result<Integer> delete(Long[] ids) {
		int delete = tbContentService.delete(ids);
		return Result.ok(delete);
	}
}
