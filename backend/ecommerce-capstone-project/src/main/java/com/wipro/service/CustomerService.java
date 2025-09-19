package com.wipro.service;

import com.wipro.model.Customer;
import com.wipro.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Customer registerCustomer(String name, String email, String password) {
        log.info("Registering new customer: {}", email);

       
        customerRepository.findByEmail(email).ifPresent(existingCustomer -> {
            throw new RuntimeException("Customer already exists with email: " + email);
        });

        
        Customer newCustomer = new Customer();
        newCustomer.setName(name);
        newCustomer.setEmail(email);
        newCustomer.setPassword(passwordEncoder.encode(password));
        newCustomer.setRole("CUSTOMER");

       
        log.info("Saving new customer to database: {}", newCustomer);
        return customerRepository.save(newCustomer);
    }

    public Customer addCustomer(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole("CUSTOMER");
        return customerRepository.save(customer);
    }

    public List<Customer> addCustomers(List<Customer> customers) {
        return customerRepository.saveAll(customers);
    }

    public Customer updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        existingCustomer.setName(customer.getName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPassword(passwordEncoder.encode(customer.getPassword()));
        existingCustomer.setNoOfOrders(customer.getNoOfOrders());
        return customerRepository.save(existingCustomer);
    }

    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
       
    }
    
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));
    }
    
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
