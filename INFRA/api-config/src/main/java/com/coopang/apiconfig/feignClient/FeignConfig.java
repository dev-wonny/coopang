package com.coopang.apiconfig.feignClient;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

//    @Bean
//    public RequestInterceptor requestInterceptor() {
//        return requestTemplate -> {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (authentication != null && authentication.getPrincipal() != null) {
//                // CommonApiHeaderFilter에서 설정된 Authentication 정보 활용
//                String userId = (String) authentication.getCredentials(); // userId
//                String role = authentication.getAuthorities().stream()
//                        .findFirst().orElseThrow(() -> new RuntimeException("Role not found")).getAuthority();
//
//                // Feign 요청 헤더에 userId와 role 추가
//                requestTemplate.header("X-User-Id", userId);
//                requestTemplate.header("X-User-Role", role.replace("ROLE_", "")); // ROLE_ prefix 제거
//            }
//        };
//    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // 현재 HttpServletRequest 가져오기
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                // 원래 요청에서 userId와 role 헤더를 가져와 Feign 요청에 추가
                String userId = request.getHeader("X-User-Id");
                String role = request.getHeader("X-User-Role");

                if (userId != null) {
                    requestTemplate.header("X-User-Id", userId);
                }
                if (role != null) {
                    requestTemplate.header("X-User-Role", role);
                }
            }
        };
    }
}