package com.coopang.ainoti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableFeignClients(basePackages = {"com.coopang.apiconfig.feignClient", "com.coopang.apicommunication.feignclient"})
@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
@ComponentScan(basePackages = {"com.coopang.ainoti", "com.coopang.apiconfig"})
@EnableMethodSecurity(securedEnabled = true)
public class AinotiApplication {
    public static void main(String[] args) {
        SpringApplication.run(AinotiApplication.class, args);
    }
}