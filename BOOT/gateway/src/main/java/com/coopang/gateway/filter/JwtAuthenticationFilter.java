package com.coopang.gateway.filter;

import static com.coopang.authcommon.constants.AuthConstants.AUTHORIZATION_HEADER;
import static com.coopang.authcommon.constants.AuthConstants.ROLE_KEY;
import static com.coopang.authcommon.constants.AuthConstants.USER_LOGIN_PATH;
import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_RESPONSE_DTO;
import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_TOKEN;
import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_USER_EMAIL;
import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_USER_ID;
import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_USER_ROLE;

import com.coopang.gateway.config.JwtProperties;
import com.coopang.gateway.error.JwtValidationException;
import com.coopang.gateway.jwt.GatewayJwtUtil;
import com.coopang.gateway.response.HeaderResponseDto;
import com.coopang.gateway.user.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter {
    private final GatewayJwtUtil jwtUtil;
    private final RedisService redisService;
    private final JwtProperties jwtProperties;

    public JwtAuthenticationFilter(GatewayJwtUtil jwtUtil, RedisService redisService, JwtProperties jwtProperties) {
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final String path = exchange.getRequest().getURI().getPath();

        return Mono.defer(() -> {
                // 1. JWT 검증이 필요 없는 URL 체크
                if (isExcludedPath(path)) {
                    return chain.filter(exchange);
                }

                // 2. 로그인 && 헤더가 없는 경우: 인증 서버로 로그인 요청
                if (path.equals(USER_LOGIN_PATH) && !StringUtils.hasText(exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER))) {
                    return chain.filter(exchange);
                }

                // 3. 헤더에서 jwt token 가져옴
                return jwtUtil.getTokenFromHeaderAsync(exchange);
            })
            // 4. 블랙리스트 검증
            .flatMap(token ->
                redisService.isBlacklistedToken(String.valueOf(token))
                    .filter(isBlackList -> !isBlackList)
                    .switchIfEmpty((Mono.error(new JwtValidationException("JWT token is blacklisted: " + token))))
                    // 5. 토큰 검증
                    .flatMap(isValid -> jwtUtil.getValidateClaimsFromTokenAsync(String.valueOf(token)))

                    .flatMap(claims -> {
                        // 6. 토큰에서 정보 추출
                        final String userId = Optional.ofNullable(claims.getSubject())
                            .orElseThrow(() -> new JwtValidationException("Invalid JWT token: Missing userId"));
                        final String userRole = Optional.ofNullable(claims.get(ROLE_KEY, String.class))
                            .orElseThrow(() -> new JwtValidationException("Invalid JWT token: Missing userRole"));
                        final String userEmail = Optional.ofNullable(claims.getId())
                            .orElseThrow(() -> new JwtValidationException("Invalid JWT token: Missing userEmail"));

                        // 7. 로그인 && 헤더가 있는 경우
                        if (path.equals(USER_LOGIN_PATH)) {
                            return exchange.getResponse().setComplete();
                        }

                        // 8. custom header 생성
                        ServerWebExchange modifiedExchange = setCustomHeader(exchange, HeaderResponseDto.of(String.valueOf(token), userEmail, userId, userRole));

                        // 9. 변경된 요청으로 체인 필터 진행
                        return chain.filter(modifiedExchange);
                    })

            )
            .onErrorMap(e -> {
                log.error("JWT 검증 중 오류 발생. 경로: {}, 에러 메시지: {}", exchange.getRequest().getURI().getPath(), e.getMessage());
                return new JwtValidationException("Error during JWT token validation: " + e.getMessage());
            });
    }

    private boolean isExcludedPath(String path) {
        return jwtProperties.getExcludedPaths().stream().anyMatch(path::contains);
    }

    private void setResponseHeader(ServerWebExchange exchange) {
        HeaderResponseDto headerResponseDto = exchange.getAttribute(HEADER_RESPONSE_DTO);

        if (!ObjectUtils.isEmpty(headerResponseDto)) {
            setCustomHeader(exchange, headerResponseDto);
            log.info("Post Filter: Added headers with UserId: {}, UserRole: {}, UserEmail: {}, Token: {}",
                headerResponseDto.getUserId()
                , headerResponseDto.getUserRole()
                , headerResponseDto.getUserEmail()
                , headerResponseDto.getToken()
            );
        }
    }

    private ServerWebExchange setCustomHeader(ServerWebExchange exchange, HeaderResponseDto responseDto) {
        exchange.mutate()
            .request(exchange.getRequest().mutate()
                .header(HEADER_USER_ID, responseDto.getUserId())
                .header(HEADER_USER_EMAIL, responseDto.getUserEmail())
                .header(HEADER_USER_ROLE, responseDto.getUserRole())
                .header(HEADER_TOKEN, responseDto.getToken())
                .build())
            .build();
        return exchange;
    }
}