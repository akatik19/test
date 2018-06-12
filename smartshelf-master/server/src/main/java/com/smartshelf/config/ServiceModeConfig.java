package com.smartshelf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.smartshelf.services.ServiceModeService;

@Configuration
public class ServiceModeConfig {

	@Bean
	public ServiceModeService serviceModeService() {
		return new ServiceModeService();
	}
}
