package com.smartshelf.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smartshelf.dao.BoxDao;
import com.smartshelf.model.Box;
import com.smartshelf.model.LEDColor;

public class ServiceModeService {

	private final Logger log = LoggerFactory.getLogger(ServiceModeService.class);
	
	private static Boolean inServiceMode = false; 
	
	@Autowired
	private ClientConnection clientConnection;
	
	@Autowired
	private BoxDao boxDao;
	
	@Value("${box.threshold.halffull}")
	private int halfFullThreshold; 
	
	public static Boolean isServiceMode() {
		return inServiceMode;
	}
	
	/***
	 * Starting the service mode
	 * In service mode empty drawers are red, 
	 * halffull drawers are yellow and full drawers are blue/green
	 * @throws Exception
	 */
	public synchronized void startServiceMode() throws Exception {
	
		if( inServiceMode ) {
			throw new Exception("Service mode already running.");
		}
		
		// shut off all blink signals
		this.clientConnection.stopAllBlinkCommands();
		
		// turn on LEDs for empty boxes
		this.clientConnection.startEmptyBoxSignals();
		
		// set status of drawers with visual feedback
		List<Box> boxes = this.boxDao.findAll();
		for( Box b : boxes ) {
			log.info("Current amount (" + b.boxid + "): " + b.amount);
			if( b.amount <= 0 ) {
				// red signal 
				System.out.print("amount: " + b.amount + " - color : " + LEDColor.RED);
				this.clientConnection.startBlinkCommand(new BlinkCommandMsg(b.boxid, LEDColor.RED.getValue()), false);
			} else if ( b.amount > 0 && b.amount <= this.halfFullThreshold ) {
				// yellow signal
				this.clientConnection.startBlinkCommand(new BlinkCommandMsg(b.boxid, LEDColor.YELLOW.getValue()), false);
			} else if ( b.amount > this.halfFullThreshold ) {
				// blue signal
				this.clientConnection.startBlinkCommand(new BlinkCommandMsg(b.boxid, LEDColor.BLUE.getValue()), false);
			}
			
			Thread.sleep(500);
		}
		
		inServiceMode = true; 
	}
	
	public synchronized void stopServiceMode() {
		
		if( !inServiceMode )
			return; 
		
		this.clientConnection.stopAllBlinkCommands();
		this.clientConnection.startEmptyBoxSignals();
		
		inServiceMode = false; 
	}
}
