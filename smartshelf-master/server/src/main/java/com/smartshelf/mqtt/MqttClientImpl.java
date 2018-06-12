package com.smartshelf.mqtt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import com.smartshelf.config.MqttConfig;

public class MqttClientImpl implements MqttService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ApplicationContext context;
	
	private MqttClient client; 
	
	@Value("${mqtt.client.clientid}")
	private String clientId; 
	@Value("${mqtt.client.brokerurl}")
	private String brokerUrl; 
	
	private Boolean tryReconnect = true;
	private static final long CONNECT_TIMEOUT = 10000;
	
	
	
	private List<String> topics; 
	
	public MqttClientImpl() {
		
		this.topics = new ArrayList<String>();
	}
	
	public MqttClientImpl(String brokerUrl, String clientId) {
		this(); 
		this.brokerUrl = brokerUrl; 
		this.clientId = clientId; 
	}
	
	@Override
	public void connect() throws MqttException {
		
		log.info(String.format("Connecting MqttClient to Broker (%s). ClientId: %s", this.brokerUrl, this.clientId));
		
		this.client = new MqttClient(this.brokerUrl, this.clientId);
		this.tryReconnect = true;
		
		while( !this.client.isConnected() && this.tryReconnect ) {
    		
        	try {
        		this.client.connect();
            	
            	log.info("Mqtt client connected.");
            	
            	MqttCallbackImpl mqttCallback = (MqttCallbackImpl)context.getBean(MqttCallbackImpl.class);
    			this.setMessageCallback(mqttCallback);

    			List<String> topics = MqttConfig.getStdTopics();
    			
    			for( String topic : topics ) {
    				client.subscribe(topic);
    			}
    			
    		} catch (MqttException e) {
    			log.error(e.getMessage());
    			log.error("Next try to connect in " + CONNECT_TIMEOUT + "ms.");
    			
    			try {
					Thread.sleep(CONNECT_TIMEOUT);
				} catch (InterruptedException e1) {
					log.error(e1.getMessage());
				}
    		}
    	}
	}

	@Override
	public void disconnect() {

		log.info("Disconnecting MqttClient");
		try {
			
			this.client.disconnect();
			this.client = null;
			this.tryReconnect = false;
			
		} catch (MqttException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public void subscribe(String topic) throws MqttException {
		
		log.info(String.format("Subscrib topic '%s'", topic));
		
		this.client.subscribe(topic);
		this.topics.add(topic);
	}

	@Override
	public void unsubscribe(String topic) throws MqttException {
		
		log.info(String.format("Unsubscrib topic '%s'", topic));
		
		this.client.unsubscribe(topic);
		this.topics.remove(topic);
	}

	@Override
	public void publish(String topic, byte[] payload) {
		
		if( topic == null || payload == null ) {
			log.error("Topic or payload null");
			return; 
		}
		
		if( this.client == null || !this.client.isConnected() ) {
			log.error("Mqtt Client is not connected to server."); 
			return; 
		}
		
		String strPayload = "";
		try {
			strPayload = new String(payload, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			log.error(e1.getMessage());
		}
		
		log.info(String.format("Publish MqttMessage to topic '%s'. Message: %s", topic, strPayload));
		
		MqttMessage message = new MqttMessage();
		message.setPayload(payload);
		MqttTopic sendTopic = this.client.getTopic(topic);
		
		try {
			MqttDeliveryToken token = sendTopic.publish(message);
			
			log.debug(String.format("Message sent. ID: %s", token.getMessageId()));
			
		} catch (MqttException e) {
			log.error(e.getMessage());
		}
		
	}

	@Override
	public Boolean isConnected() {
		
		if( this.client != null && this.client.isConnected() ) {
			return true; 
		}
		
		return false; 
	}

	@Override
	public void setMessageCallback(MqttCallbackImpl callback) {
		
		if( this.client != null && callback != null ) {
			this.client.setCallback(callback);
		}
	}
	
	@PreDestroy
	public void destroy() {
		
		this.disconnect();
	}
}
