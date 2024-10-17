package com.coopang.apiconfig.security.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@ConfigurationProperties(prefix = "common.api.filter")
public class SecurityFilterProperties {
    private final boolean enabled;
    private List<String> paths;

    public SecurityFilterProperties(boolean enabled, List<String> paths) {
        this.enabled = enabled;
        this.paths = paths != null ? paths : new ArrayList<>();
    }
}