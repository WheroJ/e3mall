package com.wheroj.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wheroj.common.Constant;
import com.wheroj.content.service.TbContentCategoryService;
import com.wheroj.e3.mapper.TbContentCategoryMapper;
import com.wheroj.e3.pojo.EasyUITreeNode;
import com.wheroj.e3.pojo.TbContentCategory;
import com.wheroj.e3.pojo.TbContentCategoryExample;

@Service
public class TbContentCategoryServiceImpl implements TbContentCategoryService {
	
	private Logger logger = LoggerFactory.getLogger(TbContentCategoryServiceImpl.class);

	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getCategoryNodes(long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		example.createCriteria().andParentIdEqualTo(parentId);
		List<TbContentCategory> categoryGroup = tbContentCategoryMapper.selectByExample(example);
		
		List<EasyUITreeNode> nodeGroup = new ArrayList<>();
		for (TbContentCategory tbCategory : categoryGroup) {
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			easyUITreeNode.setId(tbCategory.getId());
			easyUITreeNode.setState(tbCategory.getIsParent()?"closed":"open");
			easyUITreeNode.setText(tbCategory.getName());
			
			nodeGroup.add(easyUITreeNode);
		}
		return nodeGroup;
	}

	@Override
	public TbContentCategory insert(long parentId, String name) {
		TbContentCategory tbContentCategory = new TbContentCategory();
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		tbContentCategory.setName(name);
		tbContentCategory.setParentId(parentId);
		tbContentCategory.setStatus(Constant.CATEGORY_NORMAL);
		tbContentCategory.setSortOrder(1);
		tbContentCategory.setIsParent(false);
		
		tbContentCategoryMapper.insertSelective(tbContentCategory);
		TbContentCategory parentCategory = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parentCategory.getIsParent()) {
			parentCategory.setIsParent(true);
			tbContentCategoryMapper.updateByPrimaryKeySelective(parentCategory);
		}
		return tbContentCategory;
	}

	@Override
	public TbContentCategory update(long id, String name) {
		TbContentCategory tbContentCategory = new TbContentCategory();
		tbContentCategory.setId(id);
		tbContentCategory.setUpdated(new Date());
		tbContentCategory.setName(name);
		tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
		return tbContentCategory;
	}
	
	@Override
	public int delete(long id) {
		TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(id);
		if (category.getIsParent()) {
			TbContentCategoryExample example = new TbContentCategoryExample();
			example.createCriteria().andParentIdEqualTo(id);
			List<TbContentCategory> child = tbContentCategoryMapper.selectByExample(example);
			if (child.isEmpty()) {
				return tbContentCategoryMapper.deleteByPrimaryKey(id);
			} else {
				logger.error("删除内容分类报错：id=" + id + ", 存在" + child.size() + "个子节点");
				return 0;
			}
		} else {
			// 获取相同父节点的其他兄弟节点
			TbContentCategoryExample example = new TbContentCategoryExample();
			example.createCriteria().andParentIdEqualTo(category.getParentId());
			List<TbContentCategory> borthers = tbContentCategoryMapper.selectByExample(example);
			if (borthers.size() <= 1) {
				// 如果只有一个子节点，删除该节点后，父节点成为子节点
				TbContentCategory parentCategory = tbContentCategoryMapper.selectByPrimaryKey(category.getParentId());
				parentCategory.setIsParent(false);
				tbContentCategoryMapper.updateByPrimaryKeySelective(parentCategory);
			}
			return tbContentCategoryMapper.deleteByPrimaryKey(id);
		}
	}
}
