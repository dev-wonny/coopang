package com.coopang.apiconfig.feignclient;

import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_USER_ID;
import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_USER_ROLE;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {
    private static final ThreadLocal<String> dynamicRole = new ThreadLocal<>();

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // 현재 HttpServletRequest 가져오기
            final ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                // 원래 요청에서 userId와 role 헤더를 가져와 Feign 요청에 추가
                final String headerUserId = request.getHeader(HEADER_USER_ID);
                final String headerRole = dynamicRole.get() != null ? dynamicRole.get() : request.getHeader(HEADER_USER_ROLE);

                if (StringUtils.hasText(headerUserId)) {
                    requestTemplate.header(HEADER_USER_ID, headerUserId);
                }
                if (StringUtils.hasText(headerRole)) {
                    requestTemplate.header(HEADER_USER_ROLE, headerRole);
                }
            }
        };
    }

    // 역할을 SERVER로 변경하는 메서드
    public void changeHeaderRoleToServer() {
        // todo api-data가 상위로 모듈을 변경해야할듯, error 패키지를 이동해야함
        dynamicRole.set("SERVER");
    }

    // 역할을 초기화하는 메서드
    public void resetRole() {
        dynamicRole.remove();
    }
}