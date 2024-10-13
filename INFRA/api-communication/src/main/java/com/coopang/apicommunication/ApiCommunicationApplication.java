package com.coopang.apicommunication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients(basePackages = "com.coopang.apiconfig.feignclient")
@SpringBootApplication
@ComponentScan(basePackages = {"com.coopang.apiconfig"})
public class ApiCommunicationApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiCommunicationApplication.class, args);
    }
}
