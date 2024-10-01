package com.coopang.user.infrastructure.security.filter;

import com.coopang.user.infrastructure.jwt.JwtUtil;
import com.coopang.user.infrastructure.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        final String bearerJwtToken = jwtUtil.getTokenFromHeader(req);

        if (StringUtils.hasText(bearerJwtToken)) {
            // JWT 토큰 substring
            String jwtToken = jwtUtil.extractTokenFromBearer(bearerJwtToken);
            log.info(jwtToken);

            if (!jwtUtil.validateToken(jwtToken)) {
                log.error("유효하지 않은 JWT 토큰");
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            final Claims info = jwtUtil.getUserInfoFromToken(jwtToken);

            try {
                setAuthenticationByUserId(info.getSubject());
            } catch (Exception e) {
                log.error("인증 처리 중 오류: {}", e.getMessage());
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    public void setAuthenticationByUserId(String userId) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();

        // userId 조회 후 UserEntity 가져옴
        final Authentication authentication = createAuthenticationByUserId(userId);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthenticationByUserId(String userId) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, userId, userDetails.getAuthorities());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // 필터를 제외할 경로들
        String path = request.getRequestURI();

        // 제외할 경로 목록
        return path.startsWith("/users/v1/join")
                || path.startsWith("/auth/v1/login")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/actuator");
    }
}