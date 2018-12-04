package com.wheroj.content.service;

import java.util.List;

import com.wheroj.e3.pojo.PageVo;
import com.wheroj.e3.pojo.TbContent;

public interface TbContentService {
	public PageVo<TbContent> queryByPage(long categoryId, int page, int rows);
	
	public TbContent save(TbContent tbContent);
	
	public int update(TbContent tbContent);
	
	public int delete(Long[] ids);
	
	public List<TbContent> getContentListByCid(long categoryId);
}
