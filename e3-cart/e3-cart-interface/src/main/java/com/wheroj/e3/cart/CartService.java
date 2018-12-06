package com.wheroj.e3.cart;

import java.util.List;

import com.wheroj.common.pojo.Result;
import com.wheroj.e3.pojo.TbItem;

public interface CartService {
	
	public Result<?> addCart(String userId, Long itemId, int num);
	
	public Result<?> mergeCart(String userId, List<TbItem> tbItems);
	
	public List<TbItem> getCartList(String userId);
	
	public Long removeProduct(String userId, long itemId);
	
	public Long updateProduct(String userId, long itemId, int num);
}
