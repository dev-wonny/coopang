package com.coopang.gateway.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // Users API 그룹 설정
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
            .group("users")  // 그룹 이름 설정
            .pathsToMatch("/users/**")  // /users 경로를 매칭
            .build();
    }

    // Products API 그룹 설정
    @Bean
    public GroupedOpenApi productApi() {
        return GroupedOpenApi.builder()
            .group("products")  // 그룹 이름 설정
            .pathsToMatch("/products/**")  // /products 경로를 매칭
            .build();
    }

    // Orders API 그룹 설정
    @Bean
    public GroupedOpenApi orderApi() {
        return GroupedOpenApi.builder()
            .group("orders")  // 그룹 이름 설정
            .pathsToMatch("/orders/**")  // /orders 경로를 매칭
            .build();
    }

    // Delivery API 그룹 설정
    @Bean
    public GroupedOpenApi deliveryApi() {
        return GroupedOpenApi.builder()
            .group("deliveries")  // 그룹 이름 설정
            .pathsToMatch("/deliveries/**")  // /deliveries 경로를 매칭
            .build();
    }

    // 기타 API 그룹 설정
    @Bean
    public GroupedOpenApi otherApi() {
        return GroupedOpenApi.builder()
            .group("other")  // 기타 경로 그룹
            .pathsToMatch("/auth/**", "/slack-messages/**", "/ai-request-histories/**")
            .build();
    }
}
