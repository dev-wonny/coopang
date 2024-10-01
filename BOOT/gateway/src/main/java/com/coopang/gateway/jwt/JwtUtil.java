package com.coopang.gateway.jwt;

import com.coopang.gateway.user.enums.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    @Value("${service.jwt.secret-key}")
    private String secretKey;

    @Value("${service.jwt.expiration-time}")
    private long expirationTime;

    @Value("${service.jwt.token-prefix}")
    private String bearerPrefix;

    public static final String ROLE_KEY = "role";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private Key key;
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


    @PostConstruct
    public void init() {
        // 인증 서버와 동일하게 키를 생성
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(String userId, UserRoleEnum role) {

        return bearerPrefix +
                Jwts.builder()
                        .setSubject(userId)
                        .setIssuer("gateway")
                        .claim(ROLE_KEY, role.name())
                        .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                        .setIssuedAt(new Date(System.currentTimeMillis())) // 발급일
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRole(String token) {
        return
                Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .get(ROLE_KEY, String.class);
    }

    public boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public boolean isTokenValidated(String token) {
        return !isTokenExpired(token);
    }

    // JWT 검증
    public Mono<Boolean> validateToken2(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return Mono.just(true);
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        } catch (Exception e) {
            log.error("JWT validation error: {}", e.getMessage());

        }
        return Mono.just(false);
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // JWT에서 사용자 정보 추출
    public Mono<Claims> getUserInfoFromToken2(String token) {
        return Mono.fromCallable(() -> Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody());
    }

    // 쿠키에서 JWT 추출
    public Mono<String> getTokenFromCookies(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getCookies().getFirst(AUTHORIZATION_HEADER))
                .map(cookie -> {
                    String value = cookie.getValue();
                    return URLDecoder.decode(value, StandardCharsets.UTF_8);
                });
    }

    // 헤더에서 JWT 추출
    public Mono<String> getTokenFromHeader(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER));
    }

    public String extractTokenFromExchange(ServerWebExchange exchange) {
        final String authHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER);
        // authHeader가 null인지 체크합니다.
        if (StringUtils.hasText(authHeader)) {
            log.error("AUTHORIZATION_HEADER is empty or blank");
            throw new IllegalArgumentException("Authorization header is missing or empty");
        }
        return extractTokenFromBearer(authHeader);
    }

    // JWT 토큰 substring
    public String extractTokenFromBearer(String tokenValue) {
        tokenValue = URLDecoder.decode(tokenValue, StandardCharsets.UTF_8);
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(bearerPrefix)) {
            return tokenValue.substring(bearerPrefix.length());
        }
        log.error("Invalid or missing Authorization header");
        throw new NullPointerException("Not Found Token");
    }

}