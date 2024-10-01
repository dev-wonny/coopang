package com.coopang.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CustomPostFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 이전 필터에서 설정된 요청 속성에서 값을 가져옴
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
//			setResponseHeader(exchange, response);
            log.info("Post Filter: Response status code is " + response.getStatusCode());
        }));
    }

    private void setResponseHeader(ServerWebExchange exchange, ServerHttpResponse response) {
        // 응답 헤더에 사용자 ID와 역할을 추가
        final String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
        final String role = exchange.getRequest().getHeaders().getFirst("X-Role");

        if (userId != null && role != null) {
            response.getHeaders().add("X-User-Id", userId);
            response.getHeaders().add("X-Role", role);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
