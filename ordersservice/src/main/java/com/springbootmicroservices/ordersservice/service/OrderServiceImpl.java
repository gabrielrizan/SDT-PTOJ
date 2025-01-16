package com.springbootmicroservices.ordersservice.service;

import com.springbootmicroservices.ordersservice.client.ProductServiceClient;
import com.springbootmicroservices.ordersservice.entity.Order;
import com.springbootmicroservices.ordersservice.entity.OrderItem;
import com.springbootmicroservices.ordersservice.entity.OrderStatus;
import com.springbootmicroservices.ordersservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient; // Inject the ProductServiceClient

    @Override
    public Order createOrder(String userId) {
        Order newOrder = Order.builder()
                .userId(userId)
                .status(OrderStatus.CREATED)
                .build();
        return orderRepository.save(newOrder);
    }

    @Override
    public Order addProductToOrder(Long orderId, String productId, int quantity) {
        // Validate the product ID with the ProductService
        validateProductExists(productId);

        Order order = findOrderOrThrow(orderId);

        boolean productExists = false;
        for (OrderItem item : order.getItems()) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity); // Update quantity
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            OrderItem newItem = OrderItem.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .build();
            order.getItems().add(newItem);
        }

        return orderRepository.save(order);
    }

    @Override
    public Order completeOrder(Long orderId) {
        Order order = findOrderOrThrow(orderId);

        if (order.getItems().isEmpty()) {
            throw new RuntimeException("Order must have at least one item to complete!");
        }

        order.setStatus(OrderStatus.COMPLETED);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long orderId) {
        return findOrderOrThrow(orderId);
    }

    private Order findOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
    }

    private void validateProductExists(String productId) {
        try {
            productServiceClient.verifyProductExists(productId); // Call ProductServiceClient to validate
        } catch (Exception e) {
            throw new RuntimeException("Invalid product ID: " + productId, e);
        }
    }
}
