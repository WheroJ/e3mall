package com.wheroj.common.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long totalPages;
	private long recourdCount;
	private List<SearchItem> itemList;
	public long getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}
	public long getRecourdCount() {
		return recourdCount;
	}
	public void setRecourdCount(long recourdCount) {
		this.recourdCount = recourdCount;
	}
	public List<SearchItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
}
