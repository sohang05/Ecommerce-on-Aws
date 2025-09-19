package com.wipro.controller;

import com.wipro.model.Cart;
import com.wipro.model.Customer;
import com.wipro.model.Product;
import com.wipro.service.AdminService;
import com.wipro.service.CartService;
import com.wipro.service.CustomerService;
import com.wipro.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private Environment env;

    @PostMapping("/login")
    public String adminLogin(@RequestParam String username, @RequestParam String password, Model model) {
        // Validate admin credentials
        if (username.equals(env.getProperty("admin.username")) && password.equals(env.getProperty("admin.password"))) {
            return "redirect:/admin/dashboard";  // Redirect to admin dashboard
        } else {
            model.addAttribute("errorMessage", "Admin not found");
            return "admin-login";  // Show error message on login page
        }
    }

    @PostMapping("/assign-role/{customerId}")
    public ResponseEntity<?> assignRole(@PathVariable Long customerId, @RequestParam String role) {
        try {
            Customer updated = adminService.assignRole(customerId, role);
            updated.setPassword(null);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Customer Endpoints
    @PostMapping("/customers")
    public ResponseEntity<?> addCustomers(@RequestBody List<Customer> customers) {
        List<Customer> saved = customerService.addCustomers(customers);
        saved.forEach(c -> c.setPassword(null));
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/customer")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {
        Customer saved = customerService.addCustomer(customer);
        saved.setPassword(null);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/customer/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        Customer saved = customerService.updateCustomer(id, customer);
        saved.setPassword(null);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomer(id));
    }

    @GetMapping("/customers")
    public ResponseEntity<?> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        customers.forEach(c -> c.setPassword(null));
        return ResponseEntity.ok(customers);
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("deleted");
    }

    // Product Endpoints
    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.addOrUpdateProduct(product));
    }

    @PostMapping("/products/multiple")
    public ResponseEntity<?> addMultipleProducts(@RequestBody List<Product> products) {
        return ResponseEntity.ok(productService.addOrUpdateProducts(products));
    }


    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return ResponseEntity.ok(productService.addOrUpdateProduct(product));
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("deleted");
    }

    @GetMapping("/customer/{customerId}/cart")
    public ResponseEntity<?> getCartItemsByCustomerId(@PathVariable Long customerId) {
        try {
            // Get the Cart object for the customer
            Cart cart = cartService.getCartByCustomerId(customerId);

            // Return the list of CartItems (which contains product info and quantity)
            return ResponseEntity.ok(cart.getCartItems());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}