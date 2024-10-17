package com.coopang.gateway.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class GatewayController {

    @GetMapping("/get")
    public Mono<ResponseEntity<String>> get() {
        return Mono.just(new ResponseEntity<>("get test", HttpStatus.OK));
    }

    @PostMapping("/post")
    public Mono<ResponseEntity<String>> post() {
        return Mono.just(new ResponseEntity<>("post test", HttpStatus.OK));
    }
}
