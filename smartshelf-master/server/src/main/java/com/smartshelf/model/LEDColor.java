	package com.smartshelf.model;

import net.minidev.asm.ex.ConvertException;

public enum LEDColor {

	RED(0),
	BLUE(1), 
	YELLOW(2);
	
	private final int value;
    private final static LEDColor[] values = LEDColor.values();
    
    private LEDColor(int value){
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getStr() {
    	return String.valueOf(value);
    }
    
    public static LEDColor getLEDColor(int i) {
    	return values[i];
    }
    
    public static LEDColor[] getLEDColors() {
    	return values; 
    }
    
    public static LEDColor getLEDColor(String color) {
    	
    	LEDColor c = null; 
    	
    	switch(color) {
		case "blue": 
			c = LEDColor.BLUE; 
			break; 
		case "yellow": 
			c = LEDColor.YELLOW; 
			break; 
		case "red": 
			c = LEDColor.RED; 
			break;
		default: 
			throw new ConvertException("Not supported color given.");
		}
    	
    	return c; 
    }
    
    @Override
    public String toString() {
    	return super.toString().toLowerCase();
    }
}
