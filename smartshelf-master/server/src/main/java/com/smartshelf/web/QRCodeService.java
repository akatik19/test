package com.smartshelf.web;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartshelf.model.Box;
import com.smartshelf.util.QRCodeManager;

@Controller
public class QRCodeService extends AbstractController {

	@RequestMapping(value = "/qrscanner/info/box/{boxid}", method = RequestMethod.GET)
    public String getInfo(Model model, 
    		@PathVariable("boxid") String boxid, 
    		Optional<String> searchParam) {

		model = preparePage(model, boxid, null, searchParam);
		
    	return "index";
    }
    
    @RequestMapping(value = "/qrscanner/info/qrimage", method = RequestMethod.POST)
    public String getInfoWithQRImage(Model model, @RequestParam("cameraInput") MultipartFile image) {

    	File qrImage = null;
    	String result = null;
    	String errormsg = null; 
		try {
			qrImage = QRCodeManager.convert(image);
			result = QRCodeManager.decodeQRCode(qrImage);
			
		} catch (IOException e) {
			log.error(e.getMessage());
			result = null; 
			errormsg = "No QR-Code in the given Image found or corrupt image.";
		}
    	
		model = preparePage(model, result, errormsg, null);
		
    	return "index";
    }
    
    private Model preparePage(Model model, String boxid, String errorMsg, Optional<String> searchParam) {
    	
    	Box b = null; 
    	try {
    		long id = Long.parseLong(boxid);
        	boxDao.setEntityClass(Box.class);
        	b = boxDao.findById(id); 
    	} catch(Exception e) {
    		log.debug(String.format("Bad box id submited. (%s)", boxid));
    	}
    	
    	if( b != null ) {
    		setViews(model, null, "datasheet");
    		model.addAttribute("boxid", b.boxid); 
    		model.addAttribute("itemname", b.item.name);
    		model.addAttribute("datasheet", b.item.datasheet);
    	}
    	else 
    	{
			setViews(model, "qrscanner", "errormsg"); 

    		if( errorMsg == null ) {
    			model.addAttribute("errormsg", "No box with the given ID found. Maybe this QR-Code is corrupt.");
    		} else {
    			model.addAttribute("errormsg", errorMsg);
    		}	
    	}
    	
    	if( searchParam.isPresent() ) {
    		model.addAttribute("searchParam", searchParam.get());
    	}
    	
    	return model;
    }
}
