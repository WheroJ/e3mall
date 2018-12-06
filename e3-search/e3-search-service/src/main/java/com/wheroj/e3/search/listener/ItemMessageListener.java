package com.wheroj.e3.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.wheroj.e3.search.service.SearchService;

public class ItemMessageListener implements MessageListener {
	
	Logger logger = LoggerFactory.getLogger(ItemMessageListener.class);

	@Autowired
	private SearchService searchService;
	
	private final String insert="insert";
	private final String del="del";
	
	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		long itemId;
		try {
			String text = textMessage.getText();
			String[] split = text.split(":");
			if (split.length > 1) {
				switch (split[0]) {
				case insert:
					itemId = Long.parseLong(split[1]);
					searchService.addItem(itemId);
					logger.info("增加商品接收消息");
					break;
				case del:
					itemId = Long.parseLong(split[1]);
					searchService.delItem(itemId);
					logger.info("删除商品接收消息");
					break;
				}
			}
			logger.info("增加新商品的索引成功");;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
