package com.coopang.apiconfig.security.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@ConfigurationProperties(prefix = "common.api.filter")
public class SecurityFilterProperties {
    private List<String> paths;

    public SecurityFilterProperties(List<String> paths) {
        this.paths = paths;
    }
}