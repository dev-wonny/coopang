package com.coopang.gateway.user.service;

import com.coopang.gateway.response.HeaderResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j(topic = "AuthService")
@Service
public class AuthService {
    private final WebClient webClient;

    @Value("${auth.server.url}")
    private String authServerUrl = "http://43.201.54.62:8081";

    public AuthService(WebClient.Builder webClientBuilder, @Value("${auth.server.url}") String authServerUrl) {
        this.authServerUrl = authServerUrl;
        this.webClient = webClientBuilder.baseUrl(authServerUrl).build();
    }

    public Mono<String> getRoleFromAuthService(String userId) {
        return webClient.get()
                .uri("/auth/v1/cache/users/{userId}/role", userId)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(role -> log.info("Fetched role from auth server for user_id {}: {}", userId, role))
                .doOnError(error -> log.error("Error fetching role from auth server", error));
    }

    public ServerWebExchange setCustomHeader(ServerWebExchange exchange, HeaderResponseDto responseDto) {
        return exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header("X-Token", responseDto.getToken())
                        .header("X-User-Id", responseDto.getUserId())
                        .header("X-Role", responseDto.getRole())
                        .build())
                .build();
    }

    public ResponseCookie expireAuthorizationCookie() {
        // Authorization 쿠키 삭제 (만료된 쿠키 전송)
        return ResponseCookie.from("Authorization", "")
                .maxAge(0)  // 쿠키 만료
                .path("/")  // 쿠키 경로 설정
                .build();
    }
}