package com.coopang.gateway.filter;

import static com.coopang.authcommon.constants.AuthConstants.AUTHORIZATION_HEADER;
import static com.coopang.authcommon.constants.AuthConstants.USER_LOGIN_PATH;

import com.coopang.gateway.jwt.JwtUtil;
import com.coopang.gateway.response.HeaderResponseDto;
import com.coopang.gateway.user.service.AuthService;
import com.coopang.gateway.user.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter {
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final AuthService authService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RedisService redisService, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
        this.authService = authService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // JWT 검증이 필요 없는 URL 체크
        if (isExcludedPath(path)) {
            return chain.filter(exchange);
        }


        // 로그인 && 헤더가 없는 경우: 인증 서버로 로그인 요청
        if (path.equals(USER_LOGIN_PATH) && exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER) == null) {
            return chain.filter(exchange);
        }

        // 헤더에서 jwt 가져옴
        final String token = jwtUtil.extractTokenFromExchange(exchange);

        // 1. 블랙리스트 토큰 체크
        if (isInvalidToken(token)) {
            return handleInvalidToken(exchange, "JWT token is missing or blacklisted: " + token);
        }


        // 2. jwt 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            return handleInvalidToken(exchange, "JWT token validation failed: " + token);
        }

        // 3. 토큰에서 추출
        final String userId = jwtUtil.getUserId(token);
        final String userRole = jwtUtil.getUserRole(token);
        final String userEmail = jwtUtil.getUserEmail(token);


        // 로그인 && 헤더가 있는 경우
        // 인증서버로 로그인 요청하면 재토큰 받음, 그러면 기존 토큰은 어떻게 되는가? 사용가능함
        if (path.equals(USER_LOGIN_PATH)) {
            // 위에서 토큰 검증이 통과했다면, 로그인이 가능한 것이다
            // 인증 서버로 로그인 요청 안보냄 로그인 성공했다고 하자
            // 토큰이 만료되면 토큰 검증에서 실패될거고, 클라이언트에서 기존 토큰 삭제할 것임
            // 토큰이 없는 상태로 인증서버에 로그인 요청하게 됨
            return exchange.getResponse().setComplete();
        }


        // custom header 생성
        ServerWebExchange modifiedExchange = authService.setCustomHeader(
                exchange
                , HeaderResponseDto.of(token, userEmail, userId, userRole)
        );

        // 변경된 요청으로 체인 필터 진행
        return chain.filter(modifiedExchange);
    }

    private Mono<Void> handleInvalidToken(ServerWebExchange exchange, String logMessage) {
        return handleInvalidToken(exchange, logMessage, HttpStatus.UNAUTHORIZED);
    }

    private Mono<Void> handleInvalidToken(ServerWebExchange exchange, String logMessage, HttpStatus status) {
        // 로그아웃 + 만료 토큰, 다시 로그인+ 토큰 없는 상태로 만듦
        // 401 Unauthorized 응답 반환 -> 클라이언트가 헤더 삭제
        log.warn(logMessage);
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().addCookie(authService.expireAuthorizationCookie());
        return exchange.getResponse().setComplete();
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