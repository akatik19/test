package com.smartshelf.mqtt;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;

import com.smartshelf.dao.BoxDao;
import com.smartshelf.mail.OrderManager;
import com.smartshelf.model.Box;
import com.smartshelf.model.LEDColor;
import com.smartshelf.services.ClientConnection;
import com.smartshelf.services.ServiceModeService;
import com.smartshelf.web.OrderController;

public class MessageHandler implements MqttMessageHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String strEncoding = "UTF-8"; 
	
	@Autowired
	private BoxDao boxDao; 
	
	@Autowired
	private OrderManager orderManager;
	@Autowired
	private ClientConnection clientConnection;
	@Autowired
	private ApplicationContext context;
	
	@Value("${weight.maxlength}")
	private int MAX_LAST_FLOAT_SIZE;
	@Value("${box.empty.infomail.enabled}")
	private Boolean sendBoxEmptyMail;
	@Value("${box.empty.infomail.interval}")
	private int mailReminderInterval;
	
	private static Map<Long, ArrayList<Float>> lastFloats = new HashMap<Long, ArrayList<Float>>();
	private static Map<Long, Date> lastMailSent = new HashMap<Long, Date>();
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	// after the last '/' will be the boxid
	private final String BOX_WEIGHT = "devices/weight/"; 
	
	@Override
	public void handleMessage(String topic, MqttMessage message) {
		
		try {
			
			log.info(String.format("Handle message from topic '%s': %s", topic, this.convertPayload(message.getPayload())));
			
			
			if( topic.startsWith( this.BOX_WEIGHT) ) {
				
				this.handleWeightMeasurement(topic, message);
			} 
			
			
		} catch (UnsupportedEncodingException e) {

			log.error(e.getMessage());
		}
		
	}
	
	@Async
	private void handleWeightMeasurement(String topic, MqttMessage message) {
		
		// last part of the topic is the box id
		String[] topic_parts = topic.split("/"); 
		String str_boxId = topic_parts[topic_parts.length - 1];
		long boxId = -1; 
		
		try {
			if( str_boxId != null ) {
				boxId = Long.parseLong(str_boxId); 
				boxDao.setEntityClass(Box.class);
				Box box = this.boxDao.findByBoxId(boxId);
				String payload = this.convertPayload(message.getPayload()); 
				float fPayload = Float.parseFloat(payload);
				
				addWeight(boxId, fPayload);
				
				int newAmount = (int)getNewAmount(box);
				box.setAmount(newAmount);
				this.boxDao.save(box);
				
				// send mail reminder
				if( this.sendBoxEmptyMail && newAmount <= 0 && this.isMailInterval(box.boxid)) {
					OrderController oc = (OrderController)context.getBean(OrderController.class);
					oc.markBoxEmpty(Long.toString(box.getBoxid()));
					//this.orderManager.orderSupplies(box);
					lastMailSent.put(box.boxid, new Date());
				}
				
				if( newAmount > 0 && !ServiceModeService.isServiceMode()/*&& this.clientConnection.isBlinkSignal(box.getBoxid(), LEDColor.RED.getStr())*/) {
					
					this.clientConnection.stopBlinkCommand(box.getBoxid(), LEDColor.RED.toString());
				}
			}
		
		} catch(NumberFormatException e) {
			log.warn(String.format("Topic contains no parsable box id (%s).", topic));
			return; 
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
			return;
		}catch(Exception e) {
			log.warn(e.getMessage());
		}
	}
	
	/**
	 * Check if the interval for sending a new mail reminder about a empty drawer is expired
	 * @param boxid
	 * @return
	 */
	private Boolean isMailInterval(long boxid) {
		
		Date d = lastMailSent.get(boxid);
		
		if( d == null ) {
			return true; 
		}
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(d);
		c.add(Calendar.DATE, mailReminderInterval);
		Date nextIntervall = c.getTime();
		
		if( d != null && d.after(nextIntervall) ) {
			return true; 
		}
		
		return false;
	}
	
	/***
	 * Add new weight to the given box weight list
	 * @param boxid
	 * @param weight
	 */
	private void addWeight(long boxid, float weight) {
		
		if( !lastFloats.containsKey(boxid) ) {
			ArrayList<Float> tmp = new ArrayList<Float>();
			tmp.add(weight);
			lastFloats.put(boxid, tmp);
			return;
		} 
		
		if( lastFloats.get(boxid).size() >= MAX_LAST_FLOAT_SIZE) {
			lastFloats.get(boxid).remove(0);
		}
		
		lastFloats.get(boxid).add(weight);
	}
	
	/***
	 * Get new calculated amount of box
	 * @param boxid
	 * @return
	 */
	private int getNewAmount(Box box) {
		
		int length = lastFloats.get(box.boxid).size();
		float tmpVal = 0.0f; 
		for( Float value : lastFloats.get(box.boxid) ) {
			tmpVal += value; 
		}
		
		float average = tmpVal / length; 
		int result = (int)Math.round( (average / box.item.weight) );
		
		log.info("Length: " + length);
		log.info("tmpval: " + average + " -- weight: " + box.item.weight);
		
		log.info("new amount " + result);
		
		if( average < box.item.weight )
			return 0; 
		
		
		if( result <= 0 )
			return 0; 
		else 
			return result;
	}

	/**
	 * Converts the byte[] payload to a string representation
	 * @param payload
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String convertPayload(byte[] payload) throws UnsupportedEncodingException {
		
		return new String(payload, strEncoding); 
	}
}
