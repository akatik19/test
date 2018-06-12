package com.smartshelf.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.smartshelf.mail.OrderManager;
import com.smartshelf.mail.SimpleOrderManager;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

	@Bean
	public OrderManager simpleOrderManager() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl(); 
		sender.setHost("smtp.gmail.com");
		sender.setPort(587);
		sender.setUsername("smartshelf24@gmail.com");
		sender.setPassword("smartshelf");
		
		Properties p = new Properties(); 
		p.setProperty("mail.smtp.auth", "true");
		p.setProperty("mail.smtp.starttls.enable", "true");
		p.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		sender.setJavaMailProperties(p);
		
		SimpleMailMessage smm = new SimpleMailMessage(); 
		smm.setFrom("smartshelf24@gmail.com");
		smm.setSubject("Low Stock");
		
		SimpleOrderManager som = new SimpleOrderManager(); 
		som.setMailSender(sender);
		som.setTemplateMessage(smm);
		
		return som;
	}
}
