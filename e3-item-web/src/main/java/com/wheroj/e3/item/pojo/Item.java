package com.wheroj.e3.item.pojo;

import org.apache.commons.lang3.StringUtils;

import com.wheroj.e3.pojo.TbItem;

public class Item extends TbItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Item(TbItem tbItem) {
		this.setId(tbItem.getId());
		this.setTitle(tbItem.getTitle());
		this.setSellPoint(tbItem.getSellPoint());
		this.setPrice(tbItem.getPrice());
		this.setNum(tbItem.getNum());
		this.setBarcode(tbItem.getBarcode());
		this.setImage(tbItem.getImage());
		this.setCid(tbItem.getCid());
		this.setStatus(tbItem.getStatus());
		this.setCreated(tbItem.getCreated());
		this.setUpdated(tbItem.getUpdated());
		this.setDesc(tbItem.getDesc());
	}
	
	public String[] getImages() {
		String image = this.getImage();
		if (!StringUtils.isBlank(image)) {
			return image.split(",");
		}
		return null;
	}
}
