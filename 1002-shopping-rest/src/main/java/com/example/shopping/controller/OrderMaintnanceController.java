package com.example.shopping.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopping.entity.Order;
import com.example.shopping.service.OrderMaintenanceServiceImpl;

@RestController
public class OrderMaintnanceController {
	
	private final OrderMaintenanceServiceImpl orderMaintenanceServiceImpl;
	
    public OrderMaintnanceController(OrderMaintenanceServiceImpl orderMaintenanceServiceImpl) {
        this.orderMaintenanceServiceImpl = orderMaintenanceServiceImpl;
    }
	
	@GetMapping("/api/orders")
	public List<Order> getOrders(){
		return orderMaintenanceServiceImpl.findAll();
		
	}
	@GetMapping("/api/orders/{id}")
	public Order getOrder(@PathVariable String id){
		return orderMaintenanceServiceImpl.findById(id);
		
	}
}
