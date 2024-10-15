package com.coopang.gateway.filter;

import static com.coopang.authcommon.constants.AuthConstants.AUTHORIZATION_HEADER;
import static com.coopang.authcommon.constants.AuthConstants.ROLE_KEY;
import static com.coopang.authcommon.constants.AuthConstants.USER_LOGIN_PATH;

import com.coopang.gateway.error.JwtValidationException;
import com.coopang.gateway.jwt.GatewayJwtUtil;
import com.coopang.gateway.response.HeaderResponseDto;
import com.coopang.gateway.user.service.AuthService;
import com.coopang.gateway.user.service.RedisService;
import io.jsonwebtoken.Claims;
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
        final String path = exchange.getRequest().getURI().getPath();

        // JWT 검증이 필요 없는 URL 체크
        if (isExcludedPath(path)) {
            return chain.filter(exchange);
        }

        // 로그인 && 헤더가 없는 경우: 인증 서버로 로그인 요청
        if (path.equals(USER_LOGIN_PATH) && exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER) == null) {
            return chain.filter(exchange);
        }

        // 헤더에서 jwt 가져옴
        final String token = jwtUtil.getTokenFromHeader(exchange);

        // 1. 블랙리스트 토큰 체크
        if (isInvalidToken(token)) {
            throw new JwtValidationException("JWT token is missing or blacklisted: " + token);
        }


        // 2. jwt 토큰 검증 - 비동기
        return jwtUtil.validateToken(token)
            .flatMap(isValid -> {
                if (!isValid) {
                    // JWT 검증 실패 처리
                    return Mono.error(new JwtValidationException("JWT token validation failed: " + token));
                }

                // Claims 추출 - 한 번만 수행
                Mono<Claims> claimsMono = jwtUtil.getClaimsFromExchange(exchange);

                // 3. 토큰에서 정보 추출 - 비동기 처리
                return claimsMono.flatMap(claims -> {
                    String userId = claims.getSubject();
                    String userRole = claims.get(ROLE_KEY, String.class);
                    String userEmail = claims.getId();

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
            .onErrorResume(e -> Mono.error(new JwtValidationException("Error during JWT token validation: " + e.getMessage())));
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