package com.wyl.base.textMsg;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
1）同步消费：通过调用消费者的 receive 方法从目的地中显示的读取消息数据，receive 方法会一直阻塞到消息到达。
2）异步消费：客户端可以为消费者注册一个消息监听器，以定义消息到达时所采取的措施/动作。
*/

public class ConsumerListener {
	private static Logger logger = LoggerFactory.getLogger(ConsumerListener.class);
	public static void main(String[] args) {
		receive();
	}
	public static void receive() {
		ConnectionFactory connectionFactory = null;
		Connection connection = null;
		MessageConsumer consumer = null;
		Destination destination = null;
		Session session =null;
		try {
			connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://172.20.10.10:61616");
			connection = connectionFactory.createConnection();
			//消费者必须要启动连接
			connection.start();
			/*
			 * 消费端的确认机制：开启事务的时候，自动或者客户端等机制不起作用，
			 * 如果未开启事务，则有如下情况
			 * 如果是自动确认，如果消息消费出现异常，会自动重复再发送六条消息进行消费，如果都失败，也会删除队列中的该消息视为已消费，并且会放入到死信队列当中，默认队列名称为ActiveMQ.DLQ
			 * 如果使用的是客户端确认，如果不进行确认，则消息会被重复消费而不会从broker队列中删除,出现异常后该消息不会被重复发送，但是如果有新的consumer进行再次消费时，这条消息也会被再次拉取，直至手动确认才会真正消费
			 */
			session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			destination = session.createQueue("wyl_base");
			consumer = session.createConsumer(destination);
			//设置监听器，监听队列中消息变化，可以设置多个监听器并行处理
			//如果有消息未处理，就会触发onMessage方法接收未处理的消息进行处理
			
			consumer.setMessageListener(new MessageListener(){
				@Override
				public void onMessage(Message message) {
					TextMessage msg = (TextMessage) message;
					try {
						String text = msg.getText();
						logger.info("start to handle"+text);
						if(3/0==2)
							message.acknowledge();
						logger.info("end to handle"+text);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			//阻塞主线程结束，否则监听线程就会自动关闭（监听器相当于守护线程）
			System.in.read();
		} catch (Exception e) {
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
