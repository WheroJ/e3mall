package com.wheroj.e3.service;

import com.wheroj.e3.pojo.PageVo;
import com.wheroj.e3.pojo.TbItem;
import com.wheroj.e3.pojo.TbItemDesc;

public interface TbItemService {
	public TbItem getItemById(Long id);
	
	public TbItem getItemWithDescById(long id);
	
	public PageVo<TbItem> getItemsByPage(int page, int rows);
	
	public TbItem insertItem(TbItem tbItem, String desc);
	
	public TbItemDesc getItemDesc(long itemId);
	
	public int deleteItems(Long[] ids);
	
	/**
	 * 下架
	 * @param ids
	 * @return
	 */
	public int instockItems(Long[] ids);
	
	/**
	 * 上架
	 * @param ids
	 * @return
	 */
	public int reshelfItems(Long[] ids);
	
	public int updateItem(TbItem tbItem, String desc);

	public int updateItem(TbItem tbItem, String desc, String itemParams);
}
