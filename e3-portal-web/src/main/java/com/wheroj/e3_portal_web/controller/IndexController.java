package com.wheroj.e3_portal_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheroj.content.service.TbContentService;
import com.wheroj.e3.pojo.TbContent;

@Controller
public class IndexController {

	@Value("${big_ad_category}")
	private long bigAdCategory;
	
	@Autowired
	private TbContentService tbContentService;
	
	@RequestMapping("/index.html")
	public String showIndex(Model model) {
//		PageVo<TbContent> pageVo = tbContentService.queryByPage(bigAdCategory, 0, 10);
		List<TbContent> contentList = tbContentService.getContentListByCid(bigAdCategory);
		model.addAttribute("ad1List", contentList);
		return "index";
	}
}
