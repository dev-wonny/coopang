package com.coopang.order.presentation.controller;

import com.coopang.order.presentation.request.PGRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@Tag(name = "PGController API", description = "PGController API")
@Slf4j(topic = "PGController")
@RestController
@RequestMapping("/pay/PG")
public class PGController {

    @PostMapping
    public ResponseEntity<String> tryPaytoPG(
            @RequestBody PGRequestDto requestDto
            ){
        final String result = processPaymentRequest(requestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // PG사 결제 요청을 처리하는 임시 엔드포인트 (테스트용)
    private String processPaymentRequest(PGRequestDto requestDto) {
        // 랜덤으로 결제 성공 또는 실패 결정 (50% 성공, 50% 실패)
        final int successThreshold = 50; // 성공 확률 95%
        final int randomValue = ThreadLocalRandom.current().nextInt(100); // 0부터 99까지의 랜덤 값

        return randomValue < successThreshold ? "Payment processed successfully" : "Payment failed";
    }
}
