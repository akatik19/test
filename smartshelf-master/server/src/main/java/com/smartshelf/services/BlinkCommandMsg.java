package com.smartshelf.services;

public class BlinkCommandMsg {

	public long boxid; 
	public int color; 
	
	public BlinkCommandMsg() {
		
	};
	
	public BlinkCommandMsg(long boxid, int color) {
		this.boxid = boxid; 
		this.color = color; 
	}

	public long getBoxid() {
		return boxid;
	}

	public void setBoxid(long boxid) {
		this.boxid = boxid;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
}
