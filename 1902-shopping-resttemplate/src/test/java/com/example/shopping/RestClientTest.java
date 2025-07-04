package com.example.shopping;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import com.example.shopping.entity.Product;
import com.example.shopping.input.ProductMaintenanceInput;


public class RestClientTest {
    @Test
    public void test() {
    
    	RestTemplate restTemplate
    		= new RestTemplateBuilder()
    		.rootUri("http://localhost:8080")
    		.build();
    	
    	ProductMaintenanceInput productMaintenanceInput = new ProductMaintenanceInput();
    	productMaintenanceInput.setId("px05");
    	productMaintenanceInput.setName("name1");
    	productMaintenanceInput.setPrice(1000);
    	productMaintenanceInput.setStock(10);
    
    	URI location = restTemplate
    			.postForLocation("/api/products", productMaintenanceInput);
    	
    	Product products = restTemplate.getForObject(location,Product.class);
    	
    	productMaintenanceInput.setName("name改");
    	
    	restTemplate.put(location, productMaintenanceInput);
    	
    	//restTemplate.delete(location);
    
    }
}
