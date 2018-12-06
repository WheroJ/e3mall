package com.wheroj.e3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wheroj.common.pojo.Result;
import com.wheroj.e3.search.service.SearchService;

@RestController
public class SearchItemController {
	@Autowired
	private SearchService searchService;
	
	@PostMapping("/index/item/import")
	public Result<?> importSolr() {
		return searchService.importAllData();
	}
}
