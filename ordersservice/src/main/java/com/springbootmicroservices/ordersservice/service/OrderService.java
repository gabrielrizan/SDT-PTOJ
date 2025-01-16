package com.springbootmicroservices.ordersservice.service;

import com.springbootmicroservices.ordersservice.entity.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(String userId);
    Order addProductToOrder(Long orderId, String productId, int quantity);
    Order completeOrder(Long orderId);
    List<Order> getAllOrders();
    Order getOrderById(Long orderId);
}
