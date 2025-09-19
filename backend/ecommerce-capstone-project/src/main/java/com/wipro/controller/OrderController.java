package com.wipro.controller;

import com.wipro.model.Order;
import com.wipro.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{cartId}/place")
    public Order placeOrder(@PathVariable Long cartId, @RequestParam String shippingAddress) {
        return orderService.placeOrder(cartId, shippingAddress);
    }
    
//    @PutMapping("/{id}/update") 
//    public Order updateOrder(@PathVariable Long id, @RequestBody String shippingAddress) {
//        return orderService.updateOrder(id, shippingAddress);
//    }
//
//    @DeleteMapping("/{id}/delete") 
//    public String deleteOrder(@PathVariable Long id) {
//        return orderService.deleteOrder(id);
//    }
}