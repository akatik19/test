package com.smartshelf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smartshelf.services.ServiceModeService;

@Controller
public class ServiceModeController extends AbstractController {

	@Autowired
	private ServiceModeService serviceModeService; 
	
	@RequestMapping(value = "/servicemode", method = RequestMethod.GET)
	public String serviceMode(Model model) {
		
		setMaster(model, "servicemode"); 
		model.addAttribute("isServiceMode", ServiceModeService.isServiceMode() ); 
		
		return "index";
	}
	
	@RequestMapping(value = "/servicemode/start", method = RequestMethod.POST)
    public @ResponseBody Boolean startServiceMode() {

		if( !ServiceModeService.isServiceMode() ) {
			try {
				this.serviceModeService.startServiceMode();
			} catch (Exception e) {
				log.error(e.getMessage());
				return false;
			}
		}
		return true;
    }
	
	@RequestMapping(value = "/servicemode/stop", method = RequestMethod.POST) 
	public @ResponseBody Boolean stopServiceMode() {
		
		this.serviceModeService.stopServiceMode();
		return true;
	}
}
