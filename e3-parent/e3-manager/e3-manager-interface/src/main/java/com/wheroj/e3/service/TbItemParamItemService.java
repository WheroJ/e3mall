package com.wheroj.e3.service;

import com.wheroj.e3.pojo.TbItemParamItem;

public interface TbItemParamItemService {
	public TbItemParamItem selectParamItem(long itemId);
	
	public int updateParamItem(Long itemId, String itemParams) ;
}
