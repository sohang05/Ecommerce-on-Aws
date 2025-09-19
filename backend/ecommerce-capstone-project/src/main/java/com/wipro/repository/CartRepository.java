package com.wipro.repository;

import com.wipro.model.Cart;
import com.wipro.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Method to find all cart items for a specific customer
    List<Cart> findByCustomerId(Long customerId);

    // Method to delete all cart items for a specific customer
    void deleteByCustomer(Customer customer);
}
