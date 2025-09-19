package com.wipro.service;

import com.wipro.model.Cart;
import com.wipro.model.CartItem;
import com.wipro.model.Order;
import com.wipro.model.Product;
import com.wipro.repository.CartRepository;
import com.wipro.repository.CustomerRepository;
import com.wipro.repository.OrderRepository;
import com.wipro.model.Customer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CustomerRepository customerRepository;

//    public Order placeOrder(Long cartId, String shippingAddress) {
//        Optional<Cart> cartOpt = cartRepository.findById(cartId);
//
//        if (!cartOpt.isPresent()) {
//            throw new RuntimeException("Cart not found with ID: " + cartId);
//        }
//
//        Cart cart = cartOpt.get();
//
//        Order order = new Order();
//        order.setCustomer(cart.getCustomer());
//
//        // convert List<Product> to Set<Product> for Order
//        List<Product> cartProducts = cart.getProducts();
//        Set<Product> orderedProducts = new HashSet<>(cartProducts);
//        order.setProducts(cartProducts); // your Order.java allows setProducts(List<Product>)
//
//        order.setShippingAddress(shippingAddress);
//
//        // calculate total amount using each product's price and cart quantity
//        double totalAmount = 0;
//        int cartQuantity = cart.getQuantity();
//        for (Product p : cartProducts) {
//            totalAmount += p.getPrice() * cartQuantity; // p.getPrice() works
//        }
//        order.setTotalAmount(totalAmount);
//
//        return orderRepository.save(order);
//    }
    
    public Order placeOrder(Long cartId, String shippingAddress) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with ID: " + cartId));

        Customer customer = cart.getCustomer();
        
        Order order = new Order();
        order.setCustomer(cart.getCustomer());

        // Extract products from cart items
        Set<Product> orderedProducts = new HashSet<>();
        double totalAmount = 0;

        for (CartItem item : cart.getCartItems()) {
            orderedProducts.add(item.getProduct());
            totalAmount += item.getProduct().getPrice() * item.getQuantity();
        }

        order.setProducts(new ArrayList<>(orderedProducts));
        order.setShippingAddress(shippingAddress);
        order.setTotalAmount(totalAmount);
        
        Order savedOrder = orderRepository.save(order);

        // ✅ update no_of_orders
        if (customer.getNoOfOrders() == 0) {
            customer.setNoOfOrders(1);
        } else {
            customer.setNoOfOrders(customer.getNoOfOrders() + 1);
        }

        customerRepository.save(customer);

        return orderRepository.save(order);
    }

}
