package com.coopang.gateway.jwt;

import static com.coopang.authcommon.constants.AuthConstants.ROLE_KEY;

import com.coopang.authcommon.jwt.JwtCommonUtil;
import com.coopang.gateway.error.JwtValidationException;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j(topic = "GatewayJwtUtil")
@Component
public class GatewayJwtUtil {
    private final JwtCommonUtil jwtCommonUtil;

    public GatewayJwtUtil(JwtCommonUtil jwtCommonUtil) {
        this.jwtCommonUtil = jwtCommonUtil;
    }

    // 토큰 검증 - 비동기
    public Mono<Boolean> validateToken(String token) {
        return jwtCommonUtil.validateTokenAsync(token);
    }

    // 헤더에서 JWT 추출 - WebFlux
    public String getTokenFromHeader(ServerWebExchange exchange) {
        return jwtCommonUtil.extractTokenFromExchange(exchange);
    }

    // 헤더에서 JWT 추출 - WebFlux (비동기 방식으로 개선)
    public Mono<String> getTokenFromHeaderAsync(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            try {
                final String token = jwtCommonUtil.extractTokenFromExchange(exchange);
                if (!StringUtils.hasText(token)) {
                    return Mono.error(new JwtValidationException("JWT token is empty"));
                }
                return Mono.just(token);
            } catch (Exception e) {
                return Mono.error(new JwtValidationException("Error extracting JWT token: " + e.getMessage()));
            }
        });
    }

    // 토큰에서 Claims 추출 - 비동기
    public Mono<Claims> getClaimsAsync(String token) {
        return jwtCommonUtil.getClaimsAsync(token);
    }

    // 토큰 검증 & Claims 추출 - 비동기
    public Mono<Claims> getValidateClaimsFromTokenAsync(String token) {
        return jwtCommonUtil.getValidateClaimsFromTokenAsync(token);
    }

    // 헤더의 JWT에서 Claims 추출 - 비동기
    public Mono<Claims> getClaimsFromExchange(ServerWebExchange exchange) {
        return jwtCommonUtil.getClaimsAsync(jwtCommonUtil.extractTokenFromExchange(exchange));
    }

    // Claims에서 사용자 ID 추출 - 비동기
    public Mono<String> getUserIdFromClaims(Mono<Claims> claimsMono) {
        return claimsMono.map(Claims::getSubject);
    }

    // Claims에서 사용자 역할(Role) 추출 - 비동기
    public Mono<String> getUserRoleFromClaims(Mono<Claims> claimsMono) {
        return claimsMono.map(claims -> claims.get(ROLE_KEY, String.class));
    }

    // Claims에서 사용자 이메일 추출 - 비동기
    public Mono<String> getUserEmailFromClaims(Mono<Claims> claimsMono) {
        return claimsMono.map(Claims::getId);
    }

    // JWT에서 사용자 ID 추출 - 비동기
    public Mono<String> getUserId(ServerWebExchange exchange) {
        return jwtCommonUtil.getClaimsAsync(jwtCommonUtil.extractTokenFromExchange(exchange))
            .map(Claims::getSubject);
    }

    // JWT에서 사용자 역할(Role) 추출 - 비동기
    public Mono<String> getUserRole(ServerWebExchange exchange) {
        return jwtCommonUtil.getClaimsAsync(jwtCommonUtil.extractTokenFromExchange(exchange))
            .map(claims -> claims.get(ROLE_KEY, String.class));
    }

    // JWT에서 사용자 이메일 추출 - 비동기
    public Mono<String> getUserEmail(ServerWebExchange exchange) {
        return jwtCommonUtil.getClaimsAsync(jwtCommonUtil.extractTokenFromExchange(exchange))
            .map(Claims::getId);
    }

    // JWT 토큰 만료 여부 확인 - 비동기
    public Mono<Boolean> isTokenExpiredAsync(String token) {
        return jwtCommonUtil.isTokenExpiredAsync(token);
    }

    public ResponseCookie expireAuthorizationCookie() {
        return jwtCommonUtil.expireAuthorizationCookie();
    }
}