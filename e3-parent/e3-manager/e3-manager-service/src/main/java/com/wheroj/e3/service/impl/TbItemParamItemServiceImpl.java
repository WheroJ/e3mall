package com.wheroj.e3.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wheroj.e3.mapper.TbItemParamItemMapper;
import com.wheroj.e3.pojo.TbItemParamItem;
import com.wheroj.e3.pojo.TbItemParamItemExample;
import com.wheroj.e3.service.TbItemParamItemService;

@Service
public class TbItemParamItemServiceImpl implements TbItemParamItemService {

	@Autowired
	private TbItemParamItemMapper tbItemParamItemMapper;
	
	@Override
	public TbItemParamItem selectParamItem(long itemId) {
		TbItemParamItemExample example = new TbItemParamItemExample();
		example.createCriteria().andItemIdEqualTo(itemId);
		List<TbItemParamItem> result = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}
	
	@Override
	public int updateParamItem(Long itemId, String itemParams) {
		if (!StringUtils.isEmpty(itemParams)) {
			TbItemParamItem tbItemParamItem = new TbItemParamItem();
			tbItemParamItem.setParamData(itemParams);
			tbItemParamItem.setItemId(itemId);
			tbItemParamItem.setUpdated(new Date());
			
			TbItemParamItemExample example = new TbItemParamItemExample();
			example.createCriteria().andItemIdEqualTo(itemId);
			tbItemParamItemMapper.updateByExampleSelective(tbItemParamItem, example);
			return 1;
		}
		return 0;
	}
}
