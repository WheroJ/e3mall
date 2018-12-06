package com.wheroj.e3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheroj.e3.pojo.EasyUITreeNode;
import com.wheroj.e3.service.TbItemCatService;

@Controller
@RequestMapping("/item/cat")
public class TbItemCatController {
	
	@Autowired
	private TbItemCatService tbItemCatService;
	
	@PostMapping("/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCat(
			@RequestParam(value = "id", defaultValue = "0") int parentId) {
		return tbItemCatService.getNodesByParentId(parentId);
	} 
}
