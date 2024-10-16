package com.coopang.gateway.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private List<String> excludedPaths;
    public void setExcludedPaths(List<String> excludedPaths) {
        this.excludedPaths = excludedPaths != null ? excludedPaths : new ArrayList<>();
    }
}