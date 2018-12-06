package com.wheroj.e3.service;

import java.util.List;

import com.wheroj.e3.pojo.EasyUITreeNode;

public interface TbItemCatService {
	public List<EasyUITreeNode> getNodesByParentId(long parentId);
}
