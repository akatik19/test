package com.smartshelf.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.smartshelf.mqtt.MessageHandler;
import com.smartshelf.mqtt.MqttCallbackImpl;
import com.smartshelf.mqtt.MqttClientImpl;
import com.smartshelf.mqtt.MqttMessageHandler;
import com.smartshelf.mqtt.MqttService;
import com.smartshelf.services.ClientConnection;

@Configuration
public class MqttConfig {

	private static MqttService mqttClient = new MqttClientImpl(); 
	
	private static List<String> topics; 
	
	public MqttConfig() {
		
		topics = new ArrayList<String>(); 
		topics.add("devices/weight/#"); 
	}
	
	@Bean
	public MqttService mqttClient() {
		
		return mqttClient;
	}
	
	@Bean
	public MqttMessageHandler mqttMessageHandler() {
		return new MessageHandler(); 
	}
	
	@Bean 
	public MessageHandler messageHandler() {
		return new MessageHandler();
	}
	
	@Bean
	public MqttCallbackImpl mqttCallbackImpl() {
		return new MqttCallbackImpl(); 
	}
	
	@Bean 
	public ClientConnection clientConnection() {
		return new ClientConnection(); 
	}
	
	public static List<String> getStdTopics() {
		return topics; 
	}
}
