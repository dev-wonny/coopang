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
import org.springframework.util.StringUtils;
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
        return Mono.defer(() -> {
            final String path = exchange.getRequest().getURI().getPath();

            // 1. JWT 검증이 필요 없는 URL 체크
            if (isExcludedPath(path)) {
                return chain.filter(exchange);
            }

            // 2. 로그인 && 헤더가 없는 경우: 인증 서버로 로그인 요청
            if (path.equals(USER_LOGIN_PATH) && !StringUtils.hasText(exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER))) {
                return chain.filter(exchange);
            }

            // 3. 헤더에서 jwt token 가져옴
            return jwtUtil.getTokenFromHeaderAsync(exchange)
                // 4. 블랙리스트 검증
                .flatMap(token -> redisService.isBlacklistedToken(token)
                    .flatMap(isBlackList -> isBlackList ?
                        Mono.error(new JwtValidationException("JWT token is blacklisted: " + token)) :
                        // 5. 토큰 검증
                        jwtUtil.getValidateClaimsFromTokenAsync(token)
                            .flatMap(claims -> {
                                // 6. 토큰에서 정보 추출 - 비동기 처리
                                final String userId = claims.getSubject();
                                final String userRole = claims.get(ROLE_KEY, String.class);
                                final String userEmail = claims.getId();

                                // 7. 로그인 && 헤더가 있는 경우
                                if (path.equals(USER_LOGIN_PATH)) {
                                    return exchange.getResponse().setComplete();
                                }

                                // 8. custom header 생성
                                ServerWebExchange modifiedExchange = authService.setCustomHeader(
                                    exchange
                                    , HeaderResponseDto.of(token, userEmail, userId, userRole)
                                );

                                // 9. 변경된 요청으로 체인 필터 진행
                                return chain.filter(modifiedExchange);
                            })
                    )

                )
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
}