package com.wheroj.e3.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wheroj.common.pojo.SearchResult;
import com.wheroj.e3.search.service.SearchService;

@Controller
public class SearchController {

	@Value("${solr_page_size}")
	private int pageSize;
	
	@Autowired
	private SearchService searchService;
	
	@GetMapping("/search")
	public ModelAndView search (
			ModelAndView modelAndView, 
			String keyword,
			@RequestParam(value="page", defaultValue="1") int page)  throws Exception {
//		try {
			String query = new String(keyword.getBytes("iso-8859-1"), "utf-8");
			SearchResult searchResult = searchService.search(query, page, pageSize);
			modelAndView.addObject("query", query);
			modelAndView.addObject("itemList", searchResult.getItemList());
			modelAndView.addObject("recourdCount", searchResult.getRecourdCount());
			modelAndView.addObject("page", page);
			modelAndView.addObject("totalPages", searchResult.getTotalPages());
			modelAndView.setViewName("/search");
			return modelAndView;
//		} catch (Exception e) {
//			e.printStackTrace();
//			modelAndView.setViewName("/error/exception");
//			return modelAndView;
//		}
	}
}
