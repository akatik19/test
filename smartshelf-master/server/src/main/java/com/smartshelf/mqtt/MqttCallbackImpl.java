package com.smartshelf.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class MqttCallbackImpl implements MqttCallback {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired 
	private ApplicationContext context; 
	
	@Autowired
	private MessageHandler messageHandler;
	
	@Override
	public void connectionLost(Throwable arg0) {
		
		MqttClientImpl client = (MqttClientImpl)context.getBean(MqttClientImpl.class); 
		if( !client.isConnected() ) {
			
			try {
				client.connect();
			} catch (MqttException e) {
				log.error("Unable to connect to Mqtt Broker.");
			}
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		
		log.debug("MqttMessage: Delivery completed. MessageId: " + arg0.getMessageId());
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		
		if( this.messageHandler != null ) {
		
			this.messageHandler.handleMessage(arg0, arg1);
			
		} else {
			log.error("No MqttMessageHandler defined. Is null");
		}
	}

}
