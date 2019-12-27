package com.wyl.base.textMsg;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
	1）同步消费：通过调用消费者的 receive 方法从目的地中显示的读取消息数据，receive 方法会一直阻塞到消息到达。
	2）异步消费：客户端可以为消费者注册一个消息监听器，以定义消息到达时所采取的措施/动作。
 */
public class Consumer {
	private static Logger logger = LoggerFactory.getLogger(Consumer.class);
	public static void main(String[] args) {
		receive();
	}
	public static void receive() {
		ConnectionFactory connectionFactory = null;
		Connection connection = null;
		MessageConsumer consumer = null;
		Destination destination = null;
		Message message = null;
		Session session =null;
		try {
			connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://172.20.10.10:61616");
			connection = connectionFactory.createConnection();
			//消费者必须要启动连接
			connection.start();
			session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			destination = session.createQueue("wyl_base");
			consumer = session.createConsumer(destination);
			message = consumer.receive();
			//如果未开启事务，使用的是客户端确认，如果不进行确认，则消息会被重复消费而不会从broker队列中删除
			message.acknowledge();
			TextMessage msg = (TextMessage) message;
			logger.info("receive msg:"+msg.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}finally{
			try {
				if(consumer!=null)
					consumer.close();
				if(session!=null)
					session.close();
				if(connection!=null)
					connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
