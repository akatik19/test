package com.smartshelf.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface MqttService {

	public void connect() throws MqttException; 
	
	public Boolean isConnected(); 
	
	public void setMessageCallback(MqttCallbackImpl callback); 
	
	public void disconnect(); 
	
	public void subscribe(String topic) throws MqttException; 
	
	public void unsubscribe(String topic) throws MqttException;
	
	public void publish(String topic, byte[] payload); 
}
