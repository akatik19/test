package com.smartshelf.web;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.smartshelf.dao.BoxDao;
import com.smartshelf.dao.ItemDao;
import com.smartshelf.model.Box;
import com.smartshelf.model.Item;
import com.smartshelf.services.ClientConnection;

public class AbstractController {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected ApplicationContext context;
	@Autowired
	private Environment env;
	
	@Autowired
	protected ItemDao itemDao; 
	@Autowired 
	protected BoxDao boxDao; 
	
	@Autowired
	protected HttpServletRequest request;
	
	
	@PostConstruct
	public void init() {
		this.itemDao.setEntityClass(Item.class);
		this.boxDao.setEntityClass(Box.class);
	}
	
	protected Model setDetail(Model model, String detail) {
    	model.addAttribute("detailview", detail);
    	return model; 
    }
    
	protected Model setMaster(Model model, String master) {
    	model.addAttribute("masterview", master);
    	return model;
    }
    
	protected Model setViews(Model model, String master, String detail) {
    	model.addAttribute("masterview", master);
    	model.addAttribute("detailview", detail);
    	
    	return model;
    }
    
    /***
     * Set the variable 'serverurl' in the model for every request
     * @return
     */
    @ModelAttribute("serverurl")
    public String getLocalAddr() {
        return request.getLocalAddr();
    }
    
    @ModelAttribute("serverport")
    public String getServerPort() {
    	return env.getProperty("server.port");
    }
    
    @ModelAttribute("currentsearch")
    public int getCurrentSearchCount() {
    	return ClientConnection.getCurrentSignals().size();
    }
}
