package com.coopang.apiconfig.security.config;

import com.coopang.apiconfig.security.filter.CommonApiHeaderFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    private final SecurityFilterProperties securityFilterProperties;

    public FilterConfig(SecurityFilterProperties securityFilterProperties) {
        this.securityFilterProperties = securityFilterProperties;
    }

    @Bean
    @ConditionalOnProperty(name = "common.api.filter.enabled", havingValue = "true", matchIfMissing = false)
    @ConditionalOnMissingBean(CommonApiHeaderFilter.class)
    public CommonApiHeaderFilter commonHeaderFilter() {
        return new CommonApiHeaderFilter(securityFilterProperties.getPaths());
    }
}