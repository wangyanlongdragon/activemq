package com.wyl.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;

public class MyMessageListener implements MessageListener{

	@Override
	public void onMessage(Message message) {
		ActiveMQTextMessage text_msg = (ActiveMQTextMessage) message;
		try {
			
			String text = text_msg.getText();
			throw new JMSException("test exception");
			//System.out.println("consumer msg:"+text);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}

}
