package com.coopang.gateway.error;

import com.coopang.gateway.jwt.GatewayJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j(topic = "GlobalExceptionHandler")
@ControllerAdvice
public class GlobalExceptionHandler {

    private final GatewayJwtUtil gatewayJwtUtil;

    public GlobalExceptionHandler(GatewayJwtUtil gatewayJwtUtil) {
        this.gatewayJwtUtil = gatewayJwtUtil;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: " + e.getMessage()));
    }

    @ExceptionHandler(JwtValidationException.class)
    public Mono<ResponseEntity<String>> handleJwtValidationException(JwtValidationException e, ServerWebExchange exchange) {
        log.warn("JWT validation failed: {}", e.getMessage());
        // 쿠키 만료 설정
        exchange.getResponse().addCookie(gatewayJwtUtil.expireAuthorizationCookie());
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: " + e.getMessage()));
    }
}