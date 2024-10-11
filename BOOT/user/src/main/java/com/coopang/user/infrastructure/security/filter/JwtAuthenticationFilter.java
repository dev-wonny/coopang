package com.coopang.user.infrastructure.security.filter;

import static com.coopang.apiconfig.constants.HeaderConstants.HEADER_USER_ID;

import com.coopang.user.infrastructure.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
        setFilterProcessesUrl("/auth/v1/header");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String userId = request.getHeader(HEADER_USER_ID);

        if (StringUtils.hasText(userId)) {
            try {

                // Fetch user details using the userId from header
                final UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                // Create authentication token
                return new UsernamePasswordAuthenticationToken(
                        userDetails, userId, userDetails.getAuthorities());

            } catch (Exception e) {
                log.error("Authentication error: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return null;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        log.info("Successful authentication: {}", authResult.getName());
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        log.error("Unsuccessful authentication: {}", failed.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return super.requiresAuthentication(request, response);
    }
}