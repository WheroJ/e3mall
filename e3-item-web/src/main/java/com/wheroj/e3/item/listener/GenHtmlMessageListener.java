package com.wheroj.e3.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.wheroj.e3.item.pojo.Item;
import com.wheroj.e3.pojo.TbItem;
import com.wheroj.e3.pojo.TbItemDesc;
import com.wheroj.e3.service.TbItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class GenHtmlMessageListener implements MessageListener {

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Autowired
	private TbItemService tbItemService;
	
	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			long id = Long.parseLong(textMessage.getText());
			TbItem tbItem = tbItemService.getItemById(id);
			Item item = new Item(tbItem);
			TbItemDesc itemDesc = tbItemService.getItemDesc(id);
			
			try {
				Configuration configuration = freeMarkerConfigurer.getConfiguration();
				Template template = configuration.getTemplate("item.ftl");
				HashMap<String, Object> dataModel = new HashMap<String, Object>();
				dataModel.put("item", item);
				dataModel.put("itemDesc", itemDesc);
				FileWriter fileWriter = new FileWriter(new File("F:\\var\\html\\pages", id + ".html"));
				template.process(dataModel, fileWriter);
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TemplateException e) {
				e.printStackTrace();
			}
			
		} catch (NumberFormatException | JMSException e) {
			e.printStackTrace();
		}
	}

}
