package com.coopang.user.infrastructure.security.filter;

import com.coopang.user.infrastructure.security.UserDetailsServiceImpl;
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
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";

    private final UserDetailsServiceImpl userDetailsService;

    public HeaderFilter(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
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
        final String userId = request.getHeader(USER_ID_HEADER);
        final String role = request.getHeader(USER_ROLE_HEADER);

        if (StringUtils.hasText(userId)) {

//            final UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
            final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getUserPrincipal(), userId,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_" +role)));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.info("Authentication set for user: " + userId);

        } else {
            SecurityContextHolder.getContext().setAuthentication(null);
            log.info("No authentication set due to missing headers");
        }

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Exception during filter chain", e);
            throw e;
        }
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
