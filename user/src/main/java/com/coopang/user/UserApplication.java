package com.coopang.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
@EnableMethodSecurity(securedEnabled = true)
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
