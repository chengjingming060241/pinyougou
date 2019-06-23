package com.pinyougou.search.service.impl;


import java.util.Arrays;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.pinyougou.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pinyougou.search.service.ItemSearchService;
@Component
public class ItemDeleteListener implements MessageListener {

	@Autowired
	private ItemSearchService itemSearchService;

	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage =(ObjectMessage)message;
		try {
			Long[] goodsIds= (Long[]) objectMessage.getObject();
			System.out.println("监听获取到消息："+goodsIds);

			System.out.println("监听获取到消息(转换后)："+Arrays.asList(goodsIds));
			itemSearchService.deleteByGoodsIds(Arrays.asList(goodsIds, TbItem.class));
			System.out.println("执行索引库删除");
		} catch (JMSException e) {

			e.printStackTrace();
		}

	}

}
