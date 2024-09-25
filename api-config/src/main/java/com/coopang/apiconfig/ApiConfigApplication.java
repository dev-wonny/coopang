package com.coopang.apiconfig;

import com.coopang.apiconfig.security.config.SecurityFilterProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SecurityFilterProperties.class)
public class ApiConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiConfigApplication.class, args);
    }

}
