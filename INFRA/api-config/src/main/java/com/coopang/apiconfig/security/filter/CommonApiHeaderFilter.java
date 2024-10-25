package com.coopang.apiconfig.security.filter;

import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_TOKEN;
import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_USER_EMAIL;
import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_USER_ID;
import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_USER_ROLE;
import static com.coopang.coredata.user.constants.HeaderConstants.ROLE_PREFIX;

import com.coopang.apiconfig.security.UserMetadata;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j(topic = "CommonApiHeaderFilter")
public class CommonApiHeaderFilter extends OncePerRequestFilter {
    private final List<String> excludedPaths;

    public CommonApiHeaderFilter(List<String> excludedPaths) {
        this.excludedPaths = excludedPaths != null ? excludedPaths : new ArrayList<>();
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
        final String userRole = request.getHeader(HEADER_USER_ROLE);
        final String userEmail = request.getHeader(HEADER_USER_EMAIL);
        final String token = request.getHeader(HEADER_TOKEN);

        if (!StringUtils.hasText(userId) || !StringUtils.hasText(userRole)) {
            log.warn("Missing headers: X-User-Id or X-User-Role");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing authentication headers");
            return;
        }

        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            request.getUserPrincipal(),
            userId,
            Collections.singleton(new SimpleGrantedAuthority(ROLE_PREFIX + userRole))
        );

        if (StringUtils.hasText(userEmail) && StringUtils.hasText(token)) {
            // 부가 정보 추가 : userEmail, token
            authenticationToken.setDetails(UserMetadata.of(userId, userEmail, token));
            log.info("Authentication set for UserMetadata userId: {}, userEmail: {}, token: {}", userId, userEmail, token);
        }

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("Authentication set for user: " + userId + " with role: " + userRole);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return excludedPaths.stream().anyMatch(path::startsWith);
    }
}