package com.wheroj.content.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wheroj.common.JsonUtils;
import com.wheroj.content.jedis.JedisClient;
import com.wheroj.content.service.TbContentService;
import com.wheroj.e3.mapper.TbContentMapper;
import com.wheroj.e3.pojo.PageVo;
import com.wheroj.e3.pojo.TbContent;
import com.wheroj.e3.pojo.TbContentExample;

@Service
public class TbContentServiceImpl implements TbContentService {
	
	private Logger logger = LoggerFactory.getLogger(TbContentServiceImpl.class);

	@Autowired
	private TbContentMapper tbContentMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${content_list}")
	private String content_list;
	
	@Override
	public PageVo<TbContent> queryByPage(long categoryId, int page, int rows) {
		Page<Object> startPage = PageHelper.startPage(page, rows);
		
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<TbContent> contentGroup = tbContentMapper.selectByExampleWithBLOBs(example);
		
		PageVo<TbContent> pageVo = new PageVo<>();
		pageVo.setRows(contentGroup);
		pageVo.setTotal(startPage==null?contentGroup.size():startPage.getTotal());
		return pageVo;
	}

	@Override
	public TbContent save(TbContent tbContent) {
//		tbContent.setUpdated(new Date());
//		tbContent.setCreated(new Date());
		Long count = jedisClient.del(content_list, tbContent.getCategoryId() + "");
		logger.info("删除redis缓存数：" + count);
		tbContentMapper.insertSelective(tbContent);
		return tbContent;
	}

	@Override
	public int update(TbContent tbContent) {
//		tbContent.setUpdated(new Date());
		Long count = jedisClient.del(content_list, tbContent.getCategoryId() + "");
		logger.info("删除redis缓存数：" + count);
		return tbContentMapper.updateByPrimaryKeySelective(tbContent);
	}

	@Override
	public int delete(Long[] ids) {
		TbContentExample example = new TbContentExample();
		example.createCriteria().andIdIn(Arrays.asList(ids));
		
		List<TbContent> contentList = tbContentMapper.selectByExample(example);
		if (!contentList.isEmpty()) {
			Long count = jedisClient.del(content_list, contentList.get(0).getCategoryId() + "");
			logger.info("删除redis缓存数：" + count);
			return tbContentMapper.deleteByExample(example);
		}
		return 0;
	}

	@Override
	public List<TbContent> getContentListByCid(long categoryId) {
		try {
			String value = jedisClient.hget(content_list, categoryId + "");
			if (!StringUtils.isEmpty(value)) {
				List<TbContent> contentList = JsonUtils.jsonToObject(value, new TypeReference<ArrayList<TbContent>>() {});
				if (contentList != null) {
					return contentList;
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("redis中取数据出错", e);
		}
		
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<TbContent> contentGroup = tbContentMapper.selectByExampleWithBLOBs(example);
		try {
			JSONArray result = JsonUtils.turnJsonArray(JsonUtils.objectToJson(contentGroup));
			jedisClient.hset(content_list, categoryId + "", result.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存数据到redis出错", e);
		}
		return contentGroup;
	}
}
