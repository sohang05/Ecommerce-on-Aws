package com.wipro.controller;

import com.wipro.model.Product;
import com.wipro.repository.ProductRepository;
import com.wipro.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')") 
    public List<Product> getProducts(@RequestParam(required = false) String category) {
        if (category != null) {
            return productRepository.findByCategory(category);
        }
        return productRepository.findAll();
    }
    
   


    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product) {
        return productService.addOrUpdateProduct(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addMultiple")
    public List<Product> addMultipleProducts(@RequestBody List<Product> products) {
        return productService.addOrUpdateProducts(products); 
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/product/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id); 
        return productService.addOrUpdateProduct(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}