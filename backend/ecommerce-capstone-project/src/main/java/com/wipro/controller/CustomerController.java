package com.wipro.controller;

import com.wipro.model.Customer;
import com.wipro.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * API-only controller for customer operations (no HTML views).
 *
 * URL base: /api/customers
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Get the currently authenticated customer's profile
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String email = authentication.getName();
        Customer c = customerService.getCustomerByEmail(email);
        c.setPassword(null); // never return password
        return ResponseEntity.ok(c);
    }

    // Create a customer (admin or public if you allow)
    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CreateCustomerRequest req) {
        try {
            Customer created = customerService.registerCustomer(req.getName(), req.getEmail(), req.getPassword());
            created.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Admin: get all customers
    @GetMapping
    public ResponseEntity<?> getAllCustomers() {
        List<Customer> list = customerService.getAllCustomers().stream()
                .peek(c -> c.setPassword(null))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // Admin / read for any: get by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id) {
        try {
            Customer c = customerService.getCustomer(id);
            c.setPassword(null);
            return ResponseEntity.ok(c);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody UpdateCustomerRequest req) {
        try {
            Customer update = new Customer();
            update.setName(req.getName());
            update.setEmail(req.getEmail());
            update.setPassword(req.getPassword());
            update.setNoOfOrders(req.getNoOfOrders());
            Customer saved = customerService.updateCustomer(id, update);
            saved.setPassword(null);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Customer deleted");
    }

    // DTOs
    public static class CreateCustomerRequest {
        private String name;
        private String email;
        private String password;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class UpdateCustomerRequest {
        private String name;
        private String email;
        private String password;
        private int noOfOrders;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public int getNoOfOrders() { return noOfOrders; }
        public void setNoOfOrders(int noOfOrders) { this.noOfOrders = noOfOrders; }
    }
}
