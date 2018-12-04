package com.wheroj.content.service;

import java.util.List;

import com.wheroj.e3.pojo.EasyUITreeNode;
import com.wheroj.e3.pojo.TbContentCategory;

public interface TbContentCategoryService {
	public List<EasyUITreeNode> getCategoryNodes(long parentId);
	
	public TbContentCategory insert(long parentId, String name);
	
	public TbContentCategory update(long id, String name);
	
	public int delete(long id);
}
