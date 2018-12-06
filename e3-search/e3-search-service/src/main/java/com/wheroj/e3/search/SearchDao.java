package com.wheroj.e3.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.wheroj.common.pojo.SearchItem;
import com.wheroj.common.pojo.SearchResult;

@Repository
public class SearchDao {
	
	@Value("${solr_base_url}")
	private String solr_base_url;
	
	@Autowired
	private SolrServer solrServer;
	
	public SearchResult search(SolrQuery solrQuery) throws SolrServerException, IOException {
		QueryResponse solrResult = solrServer.query(solrQuery);
		
		SearchResult searchResult = new SearchResult();
		SolrDocumentList solrDocList = solrResult.getResults();
		searchResult.setRecourdCount(solrDocList.getNumFound());
		
		Map<String, Map<String, List<String>>> highlighting = solrResult.getHighlighting();
		List<SearchItem> searchItemList = new ArrayList<>();
		for (SolrDocument solrDocument : solrDocList) {
			SearchItem searchItem = new SearchItem();
			
			String id = (String) solrDocument.getFieldValue("id");
			String categoryName = (String)solrDocument.getFieldValue("item_category_name");
			String image = (String)solrDocument.getFieldValue("item_image");
			long price = (long)solrDocument.getFieldValue("item_price");
			String sell_point = (String)solrDocument.getFieldValue("item_sell_point");
			String title = null;
			Map<String, List<String>> map = highlighting.get(id);
			List<String> list = map.get("item_title");
			if (list == null || list.isEmpty()) {
				title = (String) solrDocument.getFieldValue("item_title");
			} else {
				title = list.get(0);
			}
			
			searchItem.setId(id);
			searchItem.setCategory_name(categoryName);
			searchItem.setImage(image);
			searchItem.setPrice(price);
			searchItem.setSell_point(sell_point);
			searchItem.setTitle(title);
			
			searchItemList.add(searchItem);
		}
		
		searchResult.setItemList(searchItemList);
		return searchResult;
	}
}
