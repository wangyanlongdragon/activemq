package com.wyl.base.textMsg;

import java.util.Random;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producer {
	private static Logger logger = LoggerFactory.getLogger(Producer.class);
	public static void main(String[] args) {
		String msg = "wyl_base_queue_data"+Math.random()*10;
		send(msg);
	}
	public static void send(String msg){
		//这里声明的都是javax下定义的接口类型，还没有具体到某个消息提供者
		ConnectionFactory connectionFactory = null;
		Connection connection = null;
		MessageProducer producer = null;
		Destination destination = null;
		Message message = null;
		Session session =null;
		try{
			//使用activemq连接工厂，参数使用用户名、密码、及tcp地址
			connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://172.20.10.10:61616");
			connection = connectionFactory.createConnection();
			//建议启动连接，不是必须的，如果发送者发现没有启动连接会自动启动。
			connection.start();
			//boolean transacted, int acknowledgeMode
			/*
			 * 参数解释：
			 * 1,transacted表示是否支持事务，true/false,开启事务后，只有进行commit()后消息才会从客户端发送到broker等待消费者进行消费
			 * 如果为true则代表第二个参数无效,第二个参数建议选择Session.SESSION_TRANSACTED(值为0)，如果为false则第二个参数必须传递正确
			 * 2,acknowledgeMode
			 * AUTO_ACKNOWLEDGE,自动确认值为1
			 * CLIENT_ACKNOWLEDGE,手动确认值为2
			 * DUPS_OK_ACKNOWLEDGE，含有多个客户端一条消息可以重复处理，值为3，不建议
			 */
			session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
			destination = session.createQueue("wyl_base");
			producer = session.createProducer(destination);
			message = session.createTextMessage(msg);
			producer.send(message);
			//if(3/0==2)
			session.commit();//只有经过提交才会生产者消息才会发送到broker
			logger.info("send msg:"+msg);
		}catch(Exception e){
			try {
				session.rollback();
			} catch (JMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				if(producer!=null)
					producer.close();
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
