package com.smartshelf.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface MqttMessageHandler {

	public void handleMessage(String topic, MqttMessage message);
}
