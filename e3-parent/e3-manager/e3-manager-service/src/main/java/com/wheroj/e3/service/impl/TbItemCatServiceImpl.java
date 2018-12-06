package com.wheroj.e3.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wheroj.e3.mapper.TbItemCatMapper;
import com.wheroj.e3.pojo.EasyUITreeNode;
import com.wheroj.e3.pojo.TbItemCat;
import com.wheroj.e3.pojo.TbItemCatExample;
import com.wheroj.e3.service.TbItemCatService;

@Service
public class TbItemCatServiceImpl implements TbItemCatService {
	
	@Autowired
	private TbItemCatMapper tbItemCatMapper;

	@Override
	public List<EasyUITreeNode> getNodesByParentId(long parentId) {
		TbItemCatExample tbItemCatExample = new TbItemCatExample();
		tbItemCatExample.createCriteria().andParentIdEqualTo(parentId);
		List<TbItemCat> resultList = tbItemCatMapper.selectByExample(tbItemCatExample);

		ArrayList<EasyUITreeNode> nodes = new ArrayList<EasyUITreeNode>();
		for (TbItemCat itemCat : resultList) {
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			easyUITreeNode.setId(itemCat.getId());
			easyUITreeNode.setState(itemCat.getIsParent()?"closed":"open");
			easyUITreeNode.setText(itemCat.getName());
			
			nodes.add(easyUITreeNode);
		}
		return nodes;
	}

}
