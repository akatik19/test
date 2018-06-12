package com.smartshelf.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BeanDefinitionTest {

	@Autowired
    ApplicationContext applicationContext;
	
	@Test
	public void listBeanDefinitions() {
		
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            System.out.println("BeanName " + beanName);
        }
	}
}
