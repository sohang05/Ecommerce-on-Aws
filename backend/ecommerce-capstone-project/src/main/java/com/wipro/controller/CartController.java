package com.wipro.controller;

import com.wipro.model.AddToCartRequest;
import com.wipro.model.Cart;
import com.wipro.model.CartItemDto;
import com.wipro.model.Customer;
import com.wipro.service.CartService;
import com.wipro.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerService customerService;

    // ✅ Get all cart items for the logged-in customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CartItemDto>> getCartItemsByCustomerId(
            @PathVariable Long customerId,
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        String email = authentication.getName();
        Customer customer = customerService.getCustomerByEmail(email);

        // Ensure the customerId matches the logged-in user
        if (!customer.getId().equals(customerId)) {
            return ResponseEntity.status(403).build();
        }

        Cart cart = cartService.getCartByCustomerId(customer.getId());

        List<CartItemDto> items = cart.getCartItems().stream()
                .map(item -> new CartItemDto(
                        cart.getId(),                 // cartId
                        item.getId(),                 // cartItemId  <- THIS WAS MISSING BEFORE
                        item.getProduct().getId(),    // productId
                        item.getProduct().getName(),
                        item.getProduct().getImageUrl(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(items);
    }

    // ✅ Add product to cart
    @PostMapping("/add-to-cart")
    public ResponseEntity<List<CartItemDto>> addProductToCart(
            @RequestBody AddToCartRequest addToCartRequest,
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        String email = authentication.getName();
        Customer customer = customerService.getCustomerByEmail(email);

        Cart cart = cartService.addProductToCart(
                customer.getId(),
                addToCartRequest.getProductId(),
                addToCartRequest.getQuantity()
        );

        List<CartItemDto> items = cart.getCartItems().stream()
                .map(item -> new CartItemDto(
                        cart.getId(),
                        item.getId(),                 // cartItemId
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getImageUrl(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(items);
    }

    // ✅ Update cart item quantity
    @PutMapping("/update-quantity")
    public ResponseEntity<List<CartItemDto>> updateCartItemQuantity(
            @RequestParam Long cartItemId,
            @RequestParam int quantity,
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        Cart updatedCart = cartService.updateCartItemQuantity(cartItemId, quantity);

        List<CartItemDto> items = updatedCart.getCartItems().stream()
                .map(item -> new CartItemDto(
                        updatedCart.getId(),
                        item.getId(),                 // cartItemId
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getImageUrl(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(items);
    }

    // ✅ Remove a cart item
    @DeleteMapping("/remove")
    public ResponseEntity<List<CartItemDto>> removeCartItem(
            @RequestParam Long cartItemId,
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        cartService.deleteCartItem(cartItemId);

        // Return updated cart
        String email = authentication.getName();
        Customer customer = customerService.getCustomerByEmail(email);
        Cart cart = cartService.getCartByCustomerId(customer.getId());

        List<CartItemDto> items = cart.getCartItems().stream()
                .map(item -> new CartItemDto(
                        cart.getId(),
                        item.getId(),                 // cartItemId
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getImageUrl(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(items);
    }
}
