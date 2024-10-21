package com.coopang.apiconfig.feignclient;

import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_USER_ID;
import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_USER_ROLE;
import static com.coopang.coredata.user.constants.HeaderConstants.SERVER_USER_ID;
import static com.coopang.coredata.user.enums.UserRoleEnum.SERVER;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
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
            if (!ObjectUtils.isEmpty(attributes)) {
                HttpServletRequest request = attributes.getRequest();

                // 원래 요청에서 userId와 role 헤더를 가져와 Feign 요청에 추가
                final String headerRole = !ObjectUtils.isEmpty(dynamicRole.get()) ? dynamicRole.get() : request.getHeader(HEADER_USER_ROLE);
                final String headerUserId = SERVER.name().equals(headerRole) ? SERVER_USER_ID : request.getHeader(HEADER_USER_ID);

                if (StringUtils.hasText(headerUserId)) {
                    requestTemplate.header(HEADER_USER_ID, headerUserId);
                }
                if (StringUtils.hasText(headerRole)) {
                    requestTemplate.header(HEADER_USER_ROLE, headerRole);
                }
            }
        };
    }

    /**
     * 역할을 SERVER 변경
     */
    public void changeHeaderRoleToServer() {
        dynamicRole.set(SERVER.name());
    }

    /**
     * 역할을 초기화
     */
    public void resetRole() {
        dynamicRole.remove();
    }
}