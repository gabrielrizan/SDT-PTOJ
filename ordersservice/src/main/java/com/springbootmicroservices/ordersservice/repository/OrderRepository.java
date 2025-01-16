package com.springbootmicroservices.ordersservice.repository;

import com.springbootmicroservices.ordersservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}