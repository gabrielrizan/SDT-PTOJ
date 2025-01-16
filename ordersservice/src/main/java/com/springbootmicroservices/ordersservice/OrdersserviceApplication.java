package com.springbootmicroservices.ordersservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class OrdersserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersserviceApplication.class, args);
    }

}
