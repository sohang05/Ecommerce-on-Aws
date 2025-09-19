package com.wipro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wipro.model.Customer;
import com.wipro.repository.CustomerRepository;

@Service
public class AdminService {
	
	   @Autowired
	   private CustomerRepository customerRepository;
	    
	   @Autowired
	   private PasswordEncoder passwordEncoder;

	   
	   public Customer assignRole(Long customerId, String role) {
	       if (!role.equals("ADMIN") && !role.equals("CUSTOMER")) {
	           throw new IllegalArgumentException("Invalid role");
	       }
	       Customer customer = customerRepository.findById(customerId)
	           .orElseThrow(() -> new RuntimeException("Customer not found"));
	       customer.setRole(role);
	       return customerRepository.save(customer);
	   }


}