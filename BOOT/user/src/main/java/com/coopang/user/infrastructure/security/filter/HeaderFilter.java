package com.coopang.user.infrastructure.security.filter;

import static com.coopang.apiconfig.constants.HeaderConstants.HEADER_USER_ID;
import static com.coopang.apiconfig.constants.HeaderConstants.HEADER_USER_ROLE;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j(topic = "HeaderFilter")
@Component
public class HeaderFilter extends OncePerRequestFilter {

    public HeaderFilter() {
    }

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String userId = request.getHeader(HEADER_USER_ID);
        final String role = request.getHeader(HEADER_USER_ROLE);

        if (!StringUtils.hasText(userId) || !StringUtils.hasText(role)) {
            log.warn("Missing headers: X-User-Id or X-User-Role");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing authentication headers");
            return;
        }

        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getUserPrincipal(),
                userId,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role))
        );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("Authentication set for user: " + userId + " with role: " + role);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        // 제외할 경로 목록
        return path.startsWith("/users/v1/join")
                || path.startsWith("/auth/v1/login")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/actuator");
    }
}