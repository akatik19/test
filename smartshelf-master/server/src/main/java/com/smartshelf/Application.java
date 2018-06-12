package com.smartshelf;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.smartshelf.mqtt.MqttService;

@SpringBootApplication
public class Application {

	private final static Logger log = LoggerFactory.getLogger(Application.class);
	
	private static ApplicationContext context;
	
	private static final String MQTT_ENABLED_PROP_FIELD = "mqtt.enabled";
	private static Boolean MQTT_ENABLED = true;
	
    public static void main(String[] args) {
        context = SpringApplication.run(Application.class, args);
        
        try {
        	MQTT_ENABLED = Boolean.valueOf(context.getEnvironment().getProperty( MQTT_ENABLED_PROP_FIELD ));
        } catch(Exception e) {
        	log.error(String.format("Missing or wrong configured field '%s' in application.properties.", MQTT_ENABLED_PROP_FIELD) );
        	MQTT_ENABLED = true;
        }
        
        if( MQTT_ENABLED ) {
        	startMqttClient();
        } else {
        	log.info("Server is starting without MQTT connection.");
        	log.info("See appliaction.properties for more information.");
        }
    }
    
    
    private static void startMqttClient() {
    
    	Thread mqttStarter = new Thread(new Runnable() {
    		
            @Override
            public void run() {
               
            	MqttService client = (MqttService) context.getBean(MqttService.class);
            	try {
					client.connect();
				} catch (MqttException e) {
					log.error(e.getMessage());
				}
            }
        });

        mqttStarter.start();
    }

}