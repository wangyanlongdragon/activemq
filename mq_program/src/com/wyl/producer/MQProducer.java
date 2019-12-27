package com.wyl.producer;


import java.util.UUID;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class MQProducer {
	private JmsTemplate jmsTemplate;
	private Destination destination;
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"jms_cfg.xml");
		MQProducer producer = (MQProducer) ctx.getBean("producer");
		producer.sendMsg("haha");
	}
	public  void sendMsg(String msg){
		jmsTemplate.send(destination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				String textMsg = UUID.randomUUID().toString().replace("-", "");
				TextMessage textMessage = null;
				try {
					System.out.println("send msg:"+textMsg);
					if(1==1)
					throw new JMSException("test produceexception");
					textMessage = session.createTextMessage(textMsg);
					session.commit();
					
				} catch (Exception e) {
					
					e.printStackTrace();
					session.rollback();
				}
				return textMessage;
			}
		});
	}
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	public Destination getDestination() {
		return destination;
	}
	public void setDestination(Destination destination) {
		this.destination = destination;
	}
	
	
}
