package com.springbootmicroservices.ordersservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain orderSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/orders/**") // Apply to "order" module endpoints only
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for testing purposes
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/orders/create", // Create a new order
                                "/api/v1/orders/{orderId}/add-product", // Add product to an order
                                "/api/v1/orders/{orderId}/complete", // Complete an order
                                "/api/v1/orders", // Get all orders
                                "/api/v1/orders/{orderId}" // Get a specific order
                        ).permitAll() // Permit all requests for these endpoints
                        .anyRequest().authenticated() // Ensure other requests are authenticated
                );

        return http.build();
    }
}
