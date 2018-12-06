package com.wheroj.e3.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wheroj.common.Constant;
import com.wheroj.common.IDUtils;
import com.wheroj.common.JsonUtils;
import com.wheroj.e3.mapper.TbItemDescMapper;
import com.wheroj.e3.mapper.TbItemMapper;
import com.wheroj.e3.pojo.PageVo;
import com.wheroj.e3.pojo.TbItem;
import com.wheroj.e3.pojo.TbItemDesc;
import com.wheroj.e3.pojo.TbItemExample;
import com.wheroj.e3.service.TbItemParamItemService;
import com.wheroj.e3.service.TbItemService;

@Service
public class TbItemServiceImpl implements TbItemService {
	
	private Logger logger = LoggerFactory.getLogger(TbItemServiceImpl.class);

	@Autowired
	private TbItemMapper tbItemMapper;
	
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	
	@Autowired
	private TbItemParamItemService paramItemService;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private ActiveMQTopic activeMQTopic;
	
	@Autowired
	private ActiveMQTopic freemarkerTopic;

	@Value("${REDIS_ITEM_INFO_PRE}")
	private String itemInfoPre;
	
	@Value("${REDIS_ITEM_DESC_PRE}")
	private String itemDescPre;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public TbItem getItemById(Long id) {
		try {
			ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
			String itemJson = (String) opsForValue.get(itemInfoPre + ":" + id + ":base");
			if (!StringUtils.isBlank(itemJson)) {
				TbItem tbItem = JsonUtils.jsonToObject(itemJson, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("redis获取itemId=" + id + "报错", e);
		}
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
		if (tbItem.getStatus() == Constant.DELETE) {
			return null;
		}
		
		try {
			ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
			opsForValue.set(itemInfoPre + ":" + id + ":base", JsonUtils.objectToJson(tbItem));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("redis保存itemId=" + id + "报错", e);
		}
		return tbItem;
	}

	@Override
	public PageVo<TbItem> getItemsByPage(int pageNum, int rows) {
		Page<?> page = PageHelper.startPage(pageNum, rows);
		TbItemExample tbItemExample = new TbItemExample();
		tbItemExample.createCriteria().andStatusNotEqualTo(Constant.DELETE);
		List<TbItem> items = tbItemMapper.selectByExample(tbItemExample);
		
		PageVo<TbItem> pageVo = new PageVo<TbItem>();
		pageVo.setRows(items);
		pageVo.setTotal(page == null?items.size():page.getTotal());
		
		return pageVo;
	}
	
	@Override
	public TbItem insertItem(TbItem tbItem, String desc) {
		long itemId = IDUtils.getId();
		tbItem.setId(itemId);
		tbItem.setStatus((byte)1);
		tbItem.setCreated(new Date());
		tbItem.setUpdated(new Date());
		
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		tbItemDescMapper.insert(tbItemDesc);
		tbItemMapper.insert(tbItem);
		
		jmsTemplate.send(activeMQTopic, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("insert:" + itemId + "");
			}
		});
		
		jmsTemplate.send(freemarkerTopic, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(itemId + "");
			}
		});
		return tbItem;
	}

	@Override
	public TbItemDesc getItemDesc(long itemId) {
		try {
			ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
			String itemJson = (String) opsForValue.get(itemDescPre + ":" + itemId + ":desc");
			if (!StringUtils.isBlank(itemJson)) {
				TbItemDesc tbIeItemDesc = JsonUtils.jsonToObject(itemJson, TbItemDesc.class);
				return tbIeItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("redis获取itemDesc itemId=" + itemId + "报错", e);
		}
		
		TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		try {
			ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
			opsForValue.set(itemDescPre + ":" + itemId + ":desc", JsonUtils.objectToJson(tbItemDesc));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("redis保存desc itemId=" + itemId + "报错", e);
		}
		return tbItemDesc;
	}

	@Override
	public int deleteItems(Long[] ids) {
		TbItemExample tbItemExample = new TbItemExample();
		tbItemExample.createCriteria().andIdIn(Arrays.asList(ids));
		List<TbItem> results = tbItemMapper.selectByExample(tbItemExample);
		for (TbItem tbItem : results) {
			tbItem.setStatus(Constant.DELETE);
			tbItem.setUpdated(new Date());
			tbItemMapper.updateByPrimaryKeySelective(tbItem);
			redisTemplate.expire(itemInfoPre + ":" + tbItem.getId() + ":base", 0, TimeUnit.SECONDS);
			redisTemplate.delete(itemInfoPre + ":" + tbItem.getId() + ":base");
			
			jmsTemplate.send(activeMQTopic, new MessageCreator() {

				@Override
				public Message createMessage(Session session) throws JMSException {
					logger.info("删除商品发送消息");
					return session.createTextMessage("del:" + tbItem.getId() + "");
				}});
		}
		return ids.length;
	}

	@Override
	public int instockItems(Long[] ids) {
		TbItemExample tbItemExample = new TbItemExample();
		tbItemExample.createCriteria().andIdIn(Arrays.asList(ids));
		List<TbItem> results = tbItemMapper.selectByExample(tbItemExample);
		
		int count = 0;
		for (TbItem tbItem : results) {
			if (tbItem.getStatus() == Constant.NORMAL) {
				tbItem.setStatus(Constant.INSTOCK);
				tbItem.setUpdated(new Date());
				count += tbItemMapper.updateByPrimaryKeySelective(tbItem);
			} else {
				logger.debug("商品 :" + tbItem.getId() + " 下架失败， status = " + tbItem.getStatus());
			}
		}
		return count;
	}

	@Override
	public int reshelfItems(Long[] ids) {
		TbItemExample tbItemExample = new TbItemExample();
		tbItemExample.createCriteria().andIdIn(Arrays.asList(ids));
		List<TbItem> results = tbItemMapper.selectByExample(tbItemExample);
		
		int count = 0;
		for (TbItem tbItem : results) {
			if (tbItem.getStatus() == Constant.INSTOCK) {
				tbItem.setUpdated(new Date());
				tbItem.setStatus(Constant.NORMAL);
				count += tbItemMapper.updateByPrimaryKeySelective(tbItem);
				redisTemplate.delete(itemInfoPre + ":" + tbItem.getId() + ":base");
			} else {
				logger.debug("商品 :" + tbItem.getId() + " 上架失败");
			}
		}
		return count;
	}

	@Override
	public int updateItem(TbItem tbItem, String desc) {
		tbItem.setStatus(Constant.NORMAL);
		tbItem.setUpdated(new Date());
		tbItemMapper.updateByPrimaryKeySelective(tbItem);
		redisTemplate.delete(itemInfoPre + ":" + tbItem.getId() + ":base");
		
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(tbItem.getId());
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setUpdated(new Date());
		tbItemDescMapper.updateByPrimaryKeySelective(tbItemDesc);
		return 1;
	}

	@Override
	public int updateItem(TbItem tbItem, String desc, String itemParams) {
		paramItemService.updateParamItem(tbItem.getId(), itemParams);
		return updateItem(tbItem, desc);
	}

	@Override
	public TbItem getItemWithDescById(long id) {
		return tbItemMapper.selectItemAndDescByPrimaryKey(id);
	}
}
