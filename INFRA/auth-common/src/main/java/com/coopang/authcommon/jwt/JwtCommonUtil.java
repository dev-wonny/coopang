package com.coopang.authcommon.jwt;

import static com.coopang.authcommon.constants.AuthConstants.AUTHORIZATION_HEADER;
import static com.coopang.authcommon.constants.AuthConstants.BEARER_PREFIX;
import static com.coopang.authcommon.constants.AuthConstants.ROLE_KEY;

import com.coopang.coredata.user.enums.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j(topic = "JwtCommonUtil")
@Component
public class JwtCommonUtil {
    private final Key key;

    @Value("${service.jwt.expiration-time}")
    private long expirationTime;//1시간

    public JwtCommonUtil(@Value("${service.jwt.secret-key}") String secretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 생성 - 동기 방식
    public String createToken(String userId, String email, UserRoleEnum userRoleEnum) {
        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(userId)
                .setId(email)
                .claim(ROLE_KEY, userRoleEnum.name())
                .setIssuer("https://auth.coopang.com/auth/v1/login")
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .setIssuedAt(new Date(System.currentTimeMillis())) // 발급일
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 검증 - 동기 방식
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            handleJwtException(e);
            return false;
        }
    }

    // 토큰 검증 & Claims 추출
    public Claims getValidateClaimsFromToken(String token) {
        try {
            return getClaims(token);
        } catch (Exception e) {
            handleJwtException(e);
            throw e;
        }
    }

    // JWT 검증 - 비동기
    public Mono<Claims> getValidateClaimsFromTokenAsync(String token) {
        return Mono.defer(() -> Mono.just(getValidateClaimsFromToken(token)));
    }

    private void handleJwtException(Exception e) {
        if (e instanceof ExpiredJwtException) {
            log.error("Expired JWT token");
        } else if (e instanceof UnsupportedJwtException) {
            log.error("Unsupported JWT token");
        } else if (e instanceof MalformedJwtException || e instanceof SignatureException) {
            log.error("Invalid JWT signature");
        } else if (e instanceof IllegalArgumentException) {
            log.error("JWT claims are empty");
        } else {
            log.error("Unexpected JWT exception", e);
        }
        throw new IllegalArgumentException("Invalid JWT token", e);
    }

    // JWT 검증 - 비동기
    public Mono<Boolean> validateTokenAsync(String token) {
        return Mono.defer(() -> Mono.just(validateToken(token)));
    }

    // Claims 추출 - 동기
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    // Claims 추출 - 비동기
    public Mono<Claims> getClaimsAsync(String token) {
        return Mono.defer(() -> Mono.just(getClaims(token)));
    }

    private String extractJwtFromHeader(String authHeader) {
        if (!StringUtils.hasText(authHeader)) {
            log.error("Authorization header is missing or empty");
            throw new IllegalArgumentException("Authorization header is missing or empty");
        }
        return extractTokenFromBearer(authHeader);
    }

    // Servlet에서 JWT 추출
    public String extractTokenFromServlet(HttpServletRequest request) {
        return extractJwtFromHeader(request.getHeader(AUTHORIZATION_HEADER));
    }

    // WebFlux에서 JWT 추출
    public String extractTokenFromExchange(ServerWebExchange exchange) {
        return extractJwtFromHeader(exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER));
    }

    // JWT 토큰에서 Bearer 제거
    public String extractTokenFromBearer(String bearerJwtToken) {
        final String decodedToken = decodeToken(bearerJwtToken);

        if (StringUtils.hasText(decodedToken) && decodedToken.startsWith(BEARER_PREFIX)) {
            log.debug("bearerJwtToken: {}", decodedToken);
            return decodedToken.substring(BEARER_PREFIX.length());
        }
        log.error("Invalid or missing Authorization header {}", decodedToken);
        throw new IllegalArgumentException("Invalid or missing Authorization header:" + decodedToken);
    }

    // JWT에서 사용자 ID 추출
    public String getUserId(String token) {
        return getClaims(token).getSubject();
    }

    // JWT에서 사용자 역할(Role) 추출
    public String getUserRole(String token) {
        return getClaims(token).get(ROLE_KEY, String.class);
    }

    // JWT에서 사용자 이메일 추출
    public String getUserEmail(String token) {
        return getClaims(token).getId();
    }

    // JWT 토큰 만료 여부 확인 - 동기
    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // JWT 토큰 만료 여부 - 비동기
    public Mono<Boolean> isTokenExpiredAsync(String token) {
        return Mono.defer(() -> Mono.just(isTokenExpired(token)));
    }

    // JWT 인코딩
    private String encodeToken(String token) {
        return URLEncoder.encode(token, StandardCharsets.UTF_8).replace("+", "%20");
    }

    // JWT 디코딩
    private String decodeToken(String token) {
        return URLDecoder.decode(token, StandardCharsets.UTF_8);
    }

    // JWT 쿠키에 저장 - Servlet
    public void addJwtToCookie(String token, HttpServletResponse res) {
        final String encodedToken = encodeToken(token);

        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, encodedToken); // Name-Value
        cookie.setPath("/");
        res.addCookie(cookie);
    }

    // JWT 쿠키에 저장 - WebFlux
    public Mono<Void> addJwtToCookie(String token, ServerWebExchange exchange) {
        final String encodedToken = encodeToken(token);

        ResponseCookie cookie = ResponseCookie.from(AUTHORIZATION_HEADER, encodedToken)
            .path("/")
            .httpOnly(true)
            .build();

        exchange.getResponse().addCookie(cookie);
        return exchange.getResponse().setComplete();
    }

    // 쿠키에서 JWT 추출 - Servlet
    public String getTokenFromCookies(HttpServletRequest req) {
        return findCookie(req.getCookies(), AUTHORIZATION_HEADER)
            .map(cookie -> decodeToken(cookie.getValue()))
            .orElse(null);
    }

    private Optional<Cookie> findCookie(Cookie[] cookies, String name) {
        if (cookies == null) {
            log.warn("No cookies found in request");
            return Optional.empty();
        }
        return Arrays.stream(cookies)
            .filter(cookie -> name.equals(cookie.getName()))
            .findFirst();
    }

    // 쿠키에서 JWT 추출 - WebFlux
    public Mono<String> getTokenFromCookies(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getCookies().getFirst(AUTHORIZATION_HEADER))
            .map(cookie -> decodeToken(cookie.getValue()));
    }

    // 쿠키 생성/수정
    public ResponseCookie createJwtCookie(String token, long maxAge) {
        final String encodedToken = encodeToken(token);

        return ResponseCookie.from(AUTHORIZATION_HEADER, encodedToken)
            .path("/")
            .httpOnly(true)
            .maxAge(maxAge) // 쿠키 만료 시간 설정 (maxAge가 0이면 만료된 쿠키)
            .build();
    }

    // JWT 쿠키 추가 - Servlet
    public void addJwtToCookie(String token, long maxAge, HttpServletResponse res) {
        ResponseCookie cookie = createJwtCookie(token, maxAge);
        res.addCookie(new Cookie(cookie.getName(), cookie.getValue()));
    }

    // JWT 쿠키 추가 - WebFlux
    public Mono<Void> addJwtToCookie(String token, long maxAge, ServerWebExchange exchange) {
        ResponseCookie cookie = createJwtCookie(token, maxAge);
        exchange.getResponse().addCookie(cookie);
        return exchange.getResponse().setComplete();
    }

    // JWT 쿠키 만료
    public ResponseCookie expireAuthorizationCookie() {
        return createJwtCookie("", 0);
    }
}
