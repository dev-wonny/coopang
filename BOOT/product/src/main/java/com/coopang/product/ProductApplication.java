package com.coopang.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableFeignClients(basePackages = {"com.coopang.apiconfig.feignClient",
    "com.coopang.apicommunication.feignclient"})
@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
@ComponentScan(basePackages = {"com.coopang.product", "com.coopang.apiconfig", "com.coopang.apicommunication"})
@EnableMethodSecurity(securedEnabled = true)
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

}
