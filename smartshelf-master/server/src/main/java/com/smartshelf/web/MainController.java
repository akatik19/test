package com.smartshelf.web;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smartshelf.model.Box;
import com.smartshelf.model.Item;
import com.smartshelf.model.LEDColor;
import com.smartshelf.services.BlinkCommandMsg;
import com.smartshelf.services.ClientConnection;

@Controller
public class MainController extends AbstractController {
	
	@Autowired
	private ClientConnection clientConnection;
	
    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public String index(Model model, @RequestParam("searchParam") Optional<String> searchParam) {
    	
    	model = this.prepareSearchPage(model, searchParam, true);
    	
        return "index";
    }
    
    public Model prepareSearchPage(Model model, Optional<String> searchParam, Boolean sendBlinkSignal) {
    	
    	// add views to index.html
    	setMaster(model, "searchTemplate");
    	
    	if( searchParam.isPresent() && !searchParam.get().isEmpty() ) {
    		
    		setDetail(model, "detailResultTemplate");
    		
    		System.out.println(searchParam.get());
    		model.addAttribute("searchParam", searchParam.get());
    		
    		List<Item> result = itemDao.findWithKeyword(searchParam.get()); 
    		model.addAttribute("searchResult", result); 
    		
    		model.addAttribute("currentSignals", clientConnection);
    		// all selected boxes
    		model.addAttribute("currentColors", ClientConnection.getSelectedSignalsAsMap());
    		
    		if( sendBlinkSignal ) {
    			try {
    				this.startSignal(result);
    			} catch(Exception e) {
    				model.addAttribute("errormsg", "Currently all available colors are selected. Please try again later.");
    			}
    		}
    	}
    	
    	return model;
    }
    
    @RequestMapping(value = "/box/select/{boxid}", method = RequestMethod.POST)
    public @ResponseBody void selectBox(@PathVariable("boxid") long boxid) {
    	
    	ClientConnection con = (ClientConnection)this.context.getBean(ClientConnection.class);
		if( con != null ) {
		
			con.selectOneSignal(boxid);
		}
    }
    
    @RequestMapping(value = "/searchdetail", method = RequestMethod.GET)
    public @ResponseBody String getSearchDetail(Model model, @RequestParam("searchParam") Optional<String> searchParam) throws Exception {
    	
    	if( searchParam.isPresent() && !searchParam.get().isEmpty() ) {
    		
    		setDetail(model, "detailResultTemplate");
    		model.addAttribute("searchParam", searchParam.get()); 
    		
    		List<Item> result = itemDao.findWithKeyword(searchParam.get()); 
    		model.addAttribute("searchResult", result); 
    		
    		return "detailResultTemplate";
    	}
    	
    	throw new Exception("No search parameter was defined.");
    }
    
    private void startSignal(List<Item> list) throws Exception {
    	
    	ClientConnection con = (ClientConnection)this.context.getBean(ClientConnection.class);
		if( con != null ) {
    		String color = ClientConnection.getFreeColor().toString().toLowerCase();
    		
    		for(Item item : list) {
    			
    			con.startBlinkCommand(item.box.getBoxid(), color);
    		}
    	}
    }
    
    @RequestMapping(value = { "/qrscanner" }, method = RequestMethod.GET)
    public String qrcodescanner(Model model) {
    	
    	setViews(model, "qrscanner", null);    	
    	return "index";
    }
    
    @RequestMapping(value = "/item/findbyid/{id}", method = RequestMethod.GET)
    public @ResponseBody Item getItem(@PathVariable("id") long id) {
    	itemDao.setEntityClass(Item.class);
    	return itemDao.findById(id);
    }
    
    @RequestMapping(value = "/box/findbyid/{boxid}", method = RequestMethod.GET)
    public @ResponseBody Box getBox(@PathVariable("boxid") long boxid) {
    	boxDao.setEntityClass(Box.class);
    	return boxDao.findById(boxid);             
    }
    
    @RequestMapping(value = "/box/signal/start/{boxid}/{color}", method = RequestMethod.POST)
    public @ResponseBody Boolean signalBox(@PathVariable("boxid") long boxid, @PathVariable("color") String color) {
    	
    	log.info("Publish blink signal for box: " + boxid);
    	
    	ClientConnection con = (ClientConnection)this.context.getBean(ClientConnection.class); 
    	
    	if( con != null ) {
    		color.toLowerCase().trim();
    		return con.startBlinkCommand(boxid, color);
    	}
    	
    	return false;
    }
    
    @RequestMapping(value = "/box/signal/stop/{boxid}/{color}", method = RequestMethod.POST)
    public @ResponseBody Boolean stopSignalBox(@PathVariable("boxid") long boxid, @PathVariable("color") String color) {
    	
    	log.info("Publish stop blink signal for box: " + boxid);
    	
    	ClientConnection con = (ClientConnection)this.context.getBean(ClientConnection.class); 
    	
    	if( con != null ) {
    		color = color.toLowerCase().trim();
    		return con.stopBlinkCommand(boxid, color);
    	}
    	
    	return false;
    }
    
    @RequestMapping(value = "/info/search/amount", method = RequestMethod.GET)
    public @ResponseBody int getCurrentSearches() {
    	return this.clientConnection.getCurrentSignals().size();
    }
    
    @RequestMapping(value = "/info/search/detailspage", method = RequestMethod.GET)
    public String getSearchDetailsPage(Model model) {
    	
    	class DataContainer {
    		public long boxid; 
    		public String color; 
    	}
    	List<BlinkCommandMsg> msgs = this.clientConnection.getCurrentSignals();    	
    	
    	List<DataContainer> result = new ArrayList<DataContainer>();
    	
    	for(BlinkCommandMsg msg : msgs) {
    		DataContainer dc = new DataContainer();
    		dc.boxid = msg.boxid; 
    		dc.color = LEDColor.getLEDColor(msg.color).toString();
    		result.add(dc);
    	}
    	model.addAttribute("searchInfo", result);
    	
    	return "searchDetailPopover :: resultList";
    }

}