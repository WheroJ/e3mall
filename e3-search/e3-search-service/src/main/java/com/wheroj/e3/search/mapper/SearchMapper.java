package com.wheroj.e3.search.mapper;

import java.util.List;

import com.wheroj.common.pojo.SearchItem;

public interface SearchMapper {
	List<SearchItem> selectAll();
	
	SearchItem selectItem(long itemId);
}
