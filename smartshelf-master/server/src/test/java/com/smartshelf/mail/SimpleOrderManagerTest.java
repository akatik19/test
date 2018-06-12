package com.smartshelf.mail;

import java.util.Date;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.smartshelf.model.Box;
import com.smartshelf.model.Item;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@IfProfileValue(name="include-mail-test", value="true")
public class SimpleOrderManagerTest {
	
	@Autowired
	private SimpleOrderManager simpleOrderManager;
	
	private String mailReceiver = "smartshelf24@gmail.com";
	
	private Box getRandomBox() {
		
		Box b = new Box(); 
		b.id = 100; 
		b.amount = new Random().nextInt(100);
		b.item = new Item(); 
		b.item.id = 211; 
		b.item.name = "Testitem"; 
		b.item.description = "Test description. This item was created for test purposes."; 
		
		return b; 
	}
	
	private SimpleMailMessage getSimpleMailMessage() {
		SimpleMailMessage smm = new SimpleMailMessage(); 
		smm.setFrom("smartshelf24@gmail.com");
		smm.setSubject("[TEST " + new Date().getTime() + "]Low Stock");
		
		return smm; 
	}
	
	@Before
	public void init() {
		this.simpleOrderManager.setTemplateMessage(this.getSimpleMailMessage());
	}
	
	@Test
	public void sendMail() throws Exception {
		simpleOrderManager.sendMail(this.mailReceiver, getRandomBox());
	}
	
	@Test
	public void sendMailAsync() throws InterruptedException {
		Thread sender = simpleOrderManager.sendMailAsync(this.mailReceiver, getRandomBox());
		
		sender.join(5000);
	}
}
