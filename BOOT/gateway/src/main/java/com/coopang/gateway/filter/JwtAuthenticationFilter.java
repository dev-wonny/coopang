package com.coopang.gateway.filter;

import static com.coopang.authcommon.constants.AuthConstants.AUTHORIZATION_HEADER;
import static com.coopang.authcommon.constants.AuthConstants.ROLE_KEY;
import static com.coopang.authcommon.constants.AuthConstants.USER_LOGIN_PATH;

import com.coopang.gateway.error.JwtValidationException;
import com.coopang.gateway.jwt.GatewayJwtUtil;
import com.coopang.gateway.response.HeaderResponseDto;
import com.coopang.gateway.user.service.AuthService;
import com.coopang.gateway.user.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter {
    private final GatewayJwtUtil jwtUtil;
    private final RedisService redisService;
    private final AuthService authService;

    public JwtAuthenticationFilter(GatewayJwtUtil jwtUtil, RedisService redisService, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
        this.authService = authService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono.just(exchange.getRequest().getURI().getPath())
            .flatMap(path -> {
                // JWT 검증이 필요 없는 URL 체크 (비동기)
                if (isExcludedPath(path)) {
                    return chain.filter(exchange);
                }

                // 로그인 && 헤더가 없는 경우: 인증 서버로 로그인 요청 (비동기)
                if (path.equals(USER_LOGIN_PATH) && exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER) == null) {
                    return chain.filter(exchange);
                }

                // 헤더에서 jwt 가져옴
                return jwtUtil.getTokenFromHeaderAsync(exchange)
                    .flatMap(token -> {
                        // 블랙리스트 토큰 체크
                        if (isInvalidToken(token)) {
                            return Mono.error(new JwtValidationException("JWT token is blacklisted: " + token));
                        }

                        // Claims 추출 및 토큰 검증
                        return jwtUtil.getValidateClaimsFromTokenAsync(token).flatMap(claims -> {
                            // 3. 토큰에서 정보 추출 - 비동기 처리
                            final String userId = claims.getSubject();
                            final String userRole = claims.get(ROLE_KEY, String.class);
                            final String userEmail = claims.getId();

                            // 로그인 && 헤더가 있는 경우
                            if (path.equals(USER_LOGIN_PATH)) {
                                return exchange.getResponse().setComplete();
                            }

                            // custom header 생성
                            ServerWebExchange modifiedExchange = authService.setCustomHeader(
                                exchange,
                                HeaderResponseDto.of(token, userEmail, userId, userRole)
                            );

                            // 변경된 요청으로 체인 필터 진행
                            return chain.filter(modifiedExchange);
                        });

                    })
                    .onErrorMap(e -> new JwtValidationException("Error during JWT token validation: " + e.getMessage()));

            });
    }


    private boolean isExcludedPath(String path) {
        return path.equals("/users/v1/join")
            || path.contains("/swagger-ui")
            || path.contains("/v3/api-docs")
            || path.contains("/actuator")
            ;
    }

    private boolean isInvalidToken(String token) {
        return token == null || redisService.isBlacklistedToken(token);
    }

}