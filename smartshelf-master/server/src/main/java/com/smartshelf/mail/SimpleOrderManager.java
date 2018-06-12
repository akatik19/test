package com.smartshelf.mail;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.smartshelf.dao.OrderOperatorDao;
import com.smartshelf.model.Box;
import com.smartshelf.model.OrderOperator;

public class SimpleOrderManager implements OrderManager {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private MailSender mailSender; 
	private SimpleMailMessage templateMessage; 
	
	@Autowired
	private OrderOperatorDao orderOperatorDao;
	
	@PostConstruct
	private void init() {
		this.orderOperatorDao.setEntityClass(OrderOperator.class);
	}
	
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }
    
    @Override
    public Thread sendMailAsync(String to, Box box) {
    	
    	Thread sender = new Thread(new Runnable() {
    	    public void run()
    	    {
    	         try {
					sendMail(to, box);
				} catch (Exception e) {
					log.error(e.getMessage());
				}
    	    }});  
	    
    	sender.start();
    	
    	return sender;
    }
    
	@Override
	public void sendMail(String to, Box box) throws Exception {

		if( to.isEmpty() || to == null || box == null ) {
			log.error("Given parameter are null");
			return; 
		}
		
		log.info("Sending mail to '" + to + "' for box ID '" + box.id + "'. Current amount: " + box.getAmount());
		
	    // Create a thread safe "copy" of the template message and customize it
        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        msg.setTo(to);
        msg.setText(
            "Dear Operator, \n\n" 
                + "in Box " + box.getId() + " are " + box.getAmount() + " items left. \n" 
                + "Item: " + box.getItem().getName() + "\n"
                + "ID: " + box.getItem().getId()
                + "\n"); 
        
        try{
            this.mailSender.send(msg);
        }
        catch(MailException ex) {
            log.error(ex.getMessage());          
        }
	}
	
	@Override 
	public void orderSupplies(Box box) {
		
		List<OrderOperator> oo = this.orderOperatorDao.findAll();
		
		if( oo != null ) {
			for(OrderOperator entry : oo) {
				try {
					this.sendMail(entry.getOperator().getMail(), box);
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		}
	}
}
