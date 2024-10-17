package com.coopang.gateway;

import com.coopang.gateway.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@EnableConfigurationProperties(JwtProperties.class)
@SpringBootApplication
@ComponentScan(basePackages = {"com.coopang.authcommon.jwt", "com.coopang.gateway"})
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}