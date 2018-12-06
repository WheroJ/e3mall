package com.wheroj.e3.cart.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.wheroj.common.JsonUtils;
import com.wheroj.common.pojo.Result;
import com.wheroj.e3.cart.CartService;
import com.wheroj.e3.pojo.TbItem;
import com.wheroj.e3.service.TbItemService;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	
	@Value("${REDIS_CART_TIMEOUT}")
	private Long REDIS_CART_TIMEOUT;
	
	@Autowired
	private TbItemService tbItemService;
	
	@Override
	public Result<?> addCart(String userId, Long itemId, int num) {
		HashOperations<String, String, Object> opsForHash = redisTemplate.opsForHash();
		Boolean hasKey = opsForHash.hasKey(REDIS_CART_PRE + ":" + userId, itemId + "");
		if (hasKey) {
			String json = (String) opsForHash.get(REDIS_CART_PRE + ":" + userId, itemId + "");
			TbItem tbItem = JsonUtils.jsonToObject(json, TbItem.class);
			tbItem.setNum(tbItem.getNum() + num);
			opsForHash.put(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
			return Result.ok(null);
		}
		TbItem tbItem = tbItemService.getItemById(itemId);
		tbItem.setNum(num);
		opsForHash.put(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
		return Result.ok(null);
	}

	@Override
	public Result<?> mergeCart(String userId, List<TbItem> tbItems) {
		for (TbItem tbItem : tbItems) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		return Result.ok(null);
	}

	@Override
	public List<TbItem> getCartList(String userId) {
		HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
		List<Object> jsonList = opsForHash.values(REDIS_CART_PRE + ":" + userId);
		
		ArrayList<TbItem> tbItems = new ArrayList<TbItem>();
		if (jsonList != null && !jsonList.isEmpty()) {
			for (int i = 0; i < jsonList.size(); i++) {
				String json = (String) jsonList.get(i);
				TbItem tbItem = JsonUtils.jsonToObject(json, TbItem.class);
				tbItems.add(tbItem);
			}
		}
		return tbItems;
	}

	@Override
	public Long removeProduct(String userId, long itemId) {
		HashOperations<String, String, Object> opsForHash = redisTemplate.opsForHash();
		Boolean hasKey = opsForHash.hasKey(REDIS_CART_PRE + ":" + userId, itemId + "");
		if (hasKey) {
			Long delete = opsForHash.delete(REDIS_CART_PRE + ":" + userId, itemId + "");
			return delete;
		}
		return 0L;
	}

	@Override
	public Long updateProduct(String userId, long itemId, int num) {
		HashOperations<String, String, Object> opsForHash = redisTemplate.opsForHash();
		String json = (String) opsForHash.get(REDIS_CART_PRE + ":" + userId, itemId + "");
		if (!StringUtils.isBlank(json)) {
			TbItem tbItem = JsonUtils.jsonToObject(json, TbItem.class);
			tbItem.setNum(num);
			opsForHash.put(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
			return 1l;
		}
		return 0l;
	}
}
