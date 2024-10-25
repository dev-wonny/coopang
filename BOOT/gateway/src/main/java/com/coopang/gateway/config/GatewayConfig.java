package com.coopang.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("user", r -> r.path("/users/**", "/auth/**")
                .uri("lb://user"))
            .route("hub", r -> r.path("/hubs/**", "/companies/**", "/shippers/**")
                .uri("lb://hub"))
            .route("product", r -> r.path("/products/**", "/productMessages/**", "/product-stocks/**", "/product-stock-histories/**")
                .uri("lb://product"))
            .route("order", r -> r.path("/orders/**", "/payments/**", "/payment-histories/**")
                .uri("lb://order"))
            .route("delivery", r -> r.path("/deliveries/**", "/delivery-route-histories/**")
                .uri("lb://delivery"))
            .route("ainoti", r -> r.path("/ai-request-histories/**", "/slack-messages/**")
                .uri("lb://ainoti"))

            // Swagger 문서 라우팅 추가
            .route("user-docs", r -> r.path("/user/docs/**")
                .uri("lb://user/v3/api-docs"))
            .route("product-docs", r -> r.path("/product/docs/**")
                .uri("lb://product/v3/api-docs"))
            .route("order-docs", r -> r.path("/order/docs/**")
                .uri("lb://order/v3/api-docs"))
            .route("delivery-docs", r -> r.path("/delivery/docs/**")
                .uri("lb://delivery/v3/api-docs"))
            .build();
    }
}