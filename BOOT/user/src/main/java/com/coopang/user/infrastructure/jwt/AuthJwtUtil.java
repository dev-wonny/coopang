package com.coopang.user.infrastructure.jwt;

import com.coopang.authcommon.jwt.JwtCommonUtil;
import com.coopang.coredata.user.enums.UserRoleEnum;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j(topic = "AuthJwtUtil")
@Component
public class AuthJwtUtil {
    private final JwtCommonUtil jwtCommonUtil;

    public AuthJwtUtil(JwtCommonUtil jwtCommonUtil) {
        this.jwtCommonUtil = jwtCommonUtil;
    }

    // 토큰 생성 - 동기
    public String createToken(String userId, String email, UserRoleEnum role) {
        return jwtCommonUtil.createToken(userId, email, role);
    }

    // 토큰 검증 - 동기
    public boolean validateToken(String token) {
        return jwtCommonUtil.validateToken(token);
    }

    // 토큰 검증 & Claims 추출
    public Claims getValidateClaimsFromToken(String token) {
        return jwtCommonUtil.getValidateClaimsFromToken(token);
    }

    // 헤더에서 JWT 추출 - Servlet
    public String getTokenFromHeader(HttpServletRequest request) {
        return jwtCommonUtil.extractTokenFromServlet(request);
    }

    // Claims 추출 - 동기
    public Claims getClaims(String token) {
        return jwtCommonUtil.getClaims(token);
    }

    // JWT에서 사용자 ID 추출 - 동기
    public String getUserId(String token) {
        return jwtCommonUtil.getUserId(token);
    }

    // JWT에서 사용자 역할(Role) 추출 - 동기
    public String getUserRole(String token) {
        return jwtCommonUtil.getUserRole(token);
    }

    // JWT에서 사용자 이메일 추출 - 동기
    public String getUserEmail(String token) {
        return jwtCommonUtil.getUserEmail(token);
    }

    // JWT 토큰 만료 여부 확인 - 동기
    public boolean isTokenValidated(String token) {
        return !jwtCommonUtil.isTokenExpired(token);
    }

    // JWT 토큰에서 Bearer 제거 - 동기
    public String extractTokenFromBearer(String bearerJwtToken) {
        return jwtCommonUtil.extractTokenFromBearer(bearerJwtToken);
    }

    // JWT 쿠키에 저장 - Servlet
    public void addJwtToCookie(String token, HttpServletResponse res) {
        jwtCommonUtil.addJwtToCookie(token, res);
    }
}