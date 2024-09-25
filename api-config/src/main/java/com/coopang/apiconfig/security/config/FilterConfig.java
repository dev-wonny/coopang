package com.coopang.apiconfig.security.config;

import com.coopang.apiconfig.security.filter.CommonApiHeaderFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final SecurityFilterProperties securityFilterProperties;


    public FilterConfig(SecurityFilterProperties securityFilterProperties) {
        this.securityFilterProperties = securityFilterProperties;
    }

    @Bean
    public CommonApiHeaderFilter headerFilter() {
        return new CommonApiHeaderFilter(securityFilterProperties.getPaths());
    }
}