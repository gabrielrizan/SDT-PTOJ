package com.springbootmicroservices.ordersservice.controller;

import com.springbootmicroservices.ordersservice.entity.Order;
import com.springbootmicroservices.ordersservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Create a new order for a user.
     */
    @PostMapping("/create")
    public Order createOrder(@RequestParam String userId) {
        return orderService.createOrder(userId);
    }

    /**
     * Add a product to an existing order.
     */
    @PostMapping("/{orderId}/add-product")
    public Order addProduct(@PathVariable Long orderId,
                            @RequestParam String productId,
                            @RequestParam int quantity) {
        return orderService.addProductToOrder(orderId, productId, quantity);
    }

    /**
     * Complete an order.
     */
    @PostMapping("/{orderId}/complete")
    public Order completeOrder(@PathVariable Long orderId) {
        return orderService.completeOrder(orderId);
    }

    /**
     * Get all orders.
     */
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    /**
     * Get a specific order by ID.
     */
    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }
}
