package com.wheroj.e3.search.service;

import com.wheroj.common.pojo.Result;
import com.wheroj.common.pojo.SearchResult;

public interface SearchService {
	public Result<?> importAllData();
	
	public SearchResult search(String keywords, int page, int rows) throws Exception;
	
	public int addItem(long itemId) throws Exception;
	
	public int delItem(long itemId) throws Exception;
}
