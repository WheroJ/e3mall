package com.wheroj.e3.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wheroj.common.pojo.Result;
import com.wheroj.common.pojo.SearchItem;
import com.wheroj.common.pojo.SearchResult;
import com.wheroj.e3.search.ErrorCode;
import com.wheroj.e3.search.SearchDao;
import com.wheroj.e3.search.mapper.SearchMapper;
import com.wheroj.e3.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchMapper searchMapper;
	
	@Autowired
	private SolrServer solrServer;
	
	@Value("${solr_base_url}")
	private String solr_base_url;
	
	@Autowired
	private SearchDao searchDao;
	
	@Override
	public Result<?> importAllData() {
		List<SearchItem> itemGroup = searchMapper.selectAll();
		if (!itemGroup.isEmpty()) {
			try {
				for (SearchItem searchItem : itemGroup) {
					SolrInputDocument solrInputDocument = new SolrInputDocument();
					
					solrInputDocument.addField("id", searchItem.getId());
					solrInputDocument.addField("item_category_name", searchItem.getCategory_name());
					solrInputDocument.addField("item_image", searchItem.getImage());
					solrInputDocument.addField("item_price", searchItem.getPrice());
					solrInputDocument.addField("item_sell_point", searchItem.getSell_point());
					solrInputDocument.addField("item_title", searchItem.getTitle());
					solrServer.add(solrInputDocument);
				}
				solrServer.commit();
			} catch (Exception e) {
				e.printStackTrace();
				Result.error(ErrorCode.ERROR_IMPORT, "导入报错");
			}
		}
		return Result.ok(null);
	}

	@Override
	public SearchResult search(String keywords, int page, int rows) throws Exception {
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery(keywords);
		solrQuery.setStart(page - 1);
		solrQuery.setRows(rows);
		solrQuery.set("df", "item_keywords");
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.addHighlightField("item_sell_point");
		solrQuery.addHighlightField("item_category_name");
		solrQuery.setHighlightSimplePre("<em style='color:red'>");
		solrQuery.setHighlightSimplePost("</em>");
		
		SearchResult searchResult = searchDao.search(solrQuery);
		searchResult.setTotalPages(searchResult.getRecourdCount()/rows);
		if (searchResult.getRecourdCount() % rows != 0) {
			long totalPages = searchResult.getTotalPages();
			searchResult.setTotalPages(++totalPages);
		}
		return searchResult;
	}

	@Override
	public int addItem(long itemId) throws Exception {
		SearchItem searchItem = searchMapper.selectItem(itemId);
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		
		solrInputDocument.addField("id", searchItem.getId());
		solrInputDocument.addField("item_category_name", searchItem.getCategory_name());
		solrInputDocument.addField("item_image", searchItem.getImage());
		solrInputDocument.addField("item_price", searchItem.getPrice());
		solrInputDocument.addField("item_sell_point", searchItem.getSell_point());
		solrInputDocument.addField("item_title", searchItem.getTitle());
		solrServer.add(solrInputDocument);
		solrServer.commit();
		return 1;
	}
	
	@Override
	public int delItem(long itemId) throws Exception {
		solrServer.deleteByQuery("id:" + itemId);
		solrServer.commit();
		return 1;
	}
}
