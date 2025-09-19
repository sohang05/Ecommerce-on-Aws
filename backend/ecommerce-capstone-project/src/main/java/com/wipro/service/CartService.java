package com.wipro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.model.Cart;
import com.wipro.model.CartItem;
import com.wipro.model.Customer;
import com.wipro.model.Product;
import com.wipro.repository.CartItemRepository;
import com.wipro.repository.CartRepository;
import com.wipro.repository.CustomerRepository;
import com.wipro.repository.ProductRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    // ✅ Get a single cart by customer
    public Cart getCartByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return cartRepository.findByCustomerId(customerId)
                .stream().findFirst()
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(customer);
                    return cartRepository.save(newCart);
                });
    }

    // ✅ Add product to cart
    public Cart addProductToCart(Long customerId, Long productId, int quantity) {
        Cart cart = getCartByCustomerId(customerId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // check if already in cart
        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getCartItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    // ✅ Update item quantity
   
    public Cart updateCartItemQuantity(Long cartItemId, int quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        return item.getCart(); // return updated cart
    }


    // ✅ Remove item
    public void deleteCartItem(Long cartItemId) {
        Cart cart = cartRepository.findAll().stream()
                .filter(c -> c.getCartItems().stream()
                        .anyMatch(i -> i.getId().equals(cartItemId)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getCartItems().removeIf(item -> item.getId().equals(cartItemId));
        cartRepository.save(cart);
    }
    
    
}
