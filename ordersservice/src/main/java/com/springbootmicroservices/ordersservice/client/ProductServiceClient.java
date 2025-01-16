package com.springbootmicroservices.ordersservice.client;

import com.springbootmicroservices.ordersservice.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Feign client interface named {@link ProductServiceClient} for interacting with the Product Service.
 * Provides methods to validate product existence.
 */
@FeignClient(name = "productservice", path = "/api/v1/products", configuration = FeignClientConfig.class)
public interface ProductServiceClient {

    /**
     * Verifies if a product exists by making a request to the product service.
     *
     * @param productId the product ID to validate
     */
    @GetMapping("/{productId}")
    void verifyProductExists(
            @PathVariable("productId") String productId,
            @RequestHeader("Authorization") String authorizationHeader);
}