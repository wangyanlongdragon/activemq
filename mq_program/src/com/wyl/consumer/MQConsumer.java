package com.wyl.consumer;

import javax.jms.Destination;

import org.springframework.jms.core.JmsTemplate;

public class MQConsumer {
	private JmsTemplate jmsTemplate;
	private Destination destination;
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
