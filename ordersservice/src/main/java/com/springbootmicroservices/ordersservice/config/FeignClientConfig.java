package com.springbootmicroservices.ordersservice.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import feign.Request;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * Configuration class for setting up Feign client components in the Order Service.
 * Configures custom error handling, object mapping, and decoding for Feign clients.
 */
@Slf4j
@Configuration
public class FeignClientConfig {

    /**
     * Provides a custom {@link ObjectMapper} bean configured with Jackson modules.
     *
     * @return a configured {@link ObjectMapper} instance
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule for date/time support

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Ignore unknown properties
        return objectMapper;
    }

    /**
     * Provides a custom {@link Decoder} bean for Feign clients.
     *
     * @param objectMapper the {@link ObjectMapper} to use for decoding
     * @return a {@link Decoder} instance
     */
    @Bean
    public Decoder feignDecoder(ObjectMapper objectMapper) {
        return new CustomDecoder(objectMapper);
    }

    /**
     * Provides a custom {@link ErrorDecoder} bean for Feign clients.
     *
     * @return a {@link ErrorDecoder} instance
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    /**
     * Custom implementation of a Feign `Decoder` to handle responses.
     */
    private static class CustomDecoder implements Decoder {

        private final ObjectMapper objectMapper;

        public CustomDecoder(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Object decode(Response response, Type type) throws IOException {
            if (response.status() == HttpStatus.UNAUTHORIZED.value()) {
                throw new FeignException.Unauthorized("Unauthorized", response.request(), response.request().body(), response.headers());
            }
            if (response.status() == HttpStatus.FORBIDDEN.value()) {
                throw new FeignException.Forbidden("Forbidden", response.request(), response.request().body(), response.headers());
            }
            if (response.status() == HttpStatus.NOT_FOUND.value()) {
                throw new FeignException.NotFound("Not Found", response.request(), response.request().body(), response.headers());
            }
            if (response.status() == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                throw new FeignException.MethodNotAllowed("Method Not Allowed", response.request(), response.request().body(), response.headers());
            }
            if (response.status() == HttpStatus.BAD_REQUEST.value()) {
                throw new FeignException.BadRequest("Bad Request", response.request(), response.request().body(), response.headers());
            }

            if (response.body() != null) {
                InputStream inputStream = response.body().asInputStream();
                return objectMapper.readValue(inputStream, objectMapper.constructType(type));
            }

            return null;
        }
    }

    /**
     * Custom implementation of a Feign `ErrorDecoder` to handle HTTP errors.
     */
    private static class CustomErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            HttpStatus status = HttpStatus.valueOf(response.status());
            if (status == HttpStatus.UNAUTHORIZED) {
                return new FeignException.Unauthorized("Unauthorized", response.request(), response.request().body(), response.headers());
            }
            if (status == HttpStatus.FORBIDDEN) {
                return new FeignException.Forbidden("Forbidden", response.request(), response.request().body(), response.headers());
            }
            if (status == HttpStatus.NOT_FOUND) {
                return new FeignException.NotFound("Not Found", response.request(), response.request().body(), response.headers());
            }
            if (status == HttpStatus.METHOD_NOT_ALLOWED) {
                return new FeignException.MethodNotAllowed("Method Not Allowed", response.request(), response.request().body(), response.headers());
            }
            return new FeignException.BadRequest("Bad Request", response.request(), response.request().body(), response.headers());
        }
    }
}
