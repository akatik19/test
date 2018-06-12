package com.smartshelf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smartshelf.mail.OrderManager;
import com.smartshelf.model.Box;
import com.smartshelf.model.ItemStatus;
import com.smartshelf.model.LEDColor;
import com.smartshelf.services.BlinkCommandMsg;
import com.smartshelf.services.ClientConnection;

@Controller
public class OrderController extends AbstractController {

	class OrderResponse {
	
		public Boolean success; 
		public String message = ""; 
		
		public OrderResponse() {
			this.success = true; 
		}
		
		public void setError(String message) {
			this.message = message; 
			this.success = false; 
		}
	}
	
	@Autowired
	private OrderManager orderManager;
	
	@RequestMapping(value = "/order/box/empty", method = RequestMethod.POST)
    public @ResponseBody OrderResponse markBoxEmpty(@RequestParam("boxid") String boxid) throws Exception {
    	
    	log.info("Box marked as empty: Box " + boxid);
    	
    	OrderResponse resp = new OrderResponse(); 
    	
    	long _boxid = Long.valueOf(boxid);
    	Box box = this.boxDao.findByBoxId(_boxid); 
    	if( box == null ) {
    		resp.setError("No box with given ID found.");
    	}
    	
    	// update box amount
    	box.setAmount(0);
    	box.item.setStatus(ItemStatus.OUT_OF_STOCK);
    	box = this.boxDao.save(box);
    	
    	orderManager.orderSupplies(box);
    	
    	// mark box as empty with visual feedback
    	ClientConnection con = (ClientConnection)this.context.getBean(ClientConnection.class); 
    	if( con != null && !ClientConnection.isEmptyMarkedBox(box.boxid) ) {
    		con.startBlinkCommand(new BlinkCommandMsg(box.boxid, LEDColor.RED.getValue()), false);
    		//String color = LEDColor.RED.toString();
    		//con.startBlinkCommand(_boxid, color);
    		ClientConnection.addEmptyMarkedBox(box.boxid);
    	}
    	
    	return resp;
    }
	
	@RequestMapping(value = "/order/box/notempty", method = RequestMethod.POST)
	public @ResponseBody OrderResponse marBoxNotEmpty(@RequestParam("boxid") String boxid, @RequestParam("newAmount") int newAmount) {
		
		long _boxid = Long.valueOf(boxid);
    	Box box = this.boxDao.findById(_boxid);
    	box.setAmount(newAmount);
    	box.item.setStatus(ItemStatus.AVAILABLE);
    	box = this.boxDao.save(box);
		
		ClientConnection con = (ClientConnection)this.context.getBean(ClientConnection.class); 
    	if( con != null ) {
    		String color = LEDColor.RED.getStr();
    		con.stopBlinkCommand(Long.valueOf(boxid), color);
    		con.removeEmptyMarkedBox(Long.valueOf(boxid));
    	}
    	
    	return new OrderResponse();
	}
}
