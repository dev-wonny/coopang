package com.coopang.order.application.service.pg;

import com.coopang.order.presentation.request.pg.PGRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j(topic = "PGService")
@Service
public class PGService {

    // PG사 결제 요청을 처리하는 임시 엔드포인트 (테스트용)
    public String processPaymentRequest(PGRequestDto requestDto) {
        // 결제 요청 로그
        log.info("Processing payment request...");
        log.info("Payment Method: {}", requestDto.getPaymentMethod());
        log.info("Order Total Price: {}", requestDto.getOrderTotalPrice());

        // 랜덤으로 결제 성공 또는 실패 결정 (50% 성공, 50% 실패)
        final int successThreshold = 50; // 성공 확률 50%
        final int randomValue = ThreadLocalRandom.current().nextInt(100); // 0부터 99까지의 랜덤 값

        if (randomValue < successThreshold) {
            // 결제 성공 로그
            log.info("Payment processed successfully. Method: {}, Total Price: {}",
                    requestDto.getPaymentMethod(), requestDto.getOrderTotalPrice());
            return "Payment processed successfully";
        } else {
            // 결제 실패 로그
            log.error("Payment processing failed. Method: {}, Total Price: {}. Reason: Random failure.",
                    requestDto.getPaymentMethod(), requestDto.getOrderTotalPrice());
            return "Payment failed";
        }
    }

    // PG사 결제 취소 요청을 처리하는 임시 메서드
    public String cancelPaymentRequest(PGRequestDto requestDto) {
        // 결제 취소 로그
        log.info("Cancelling payment...");
        log.info("Payment Method: {}", requestDto.getPaymentMethod());
        log.info("Order Total Price: {}", requestDto.getOrderTotalPrice());

        String result = "Payment cancelled";

        // 결제 취소 성공 로그
        log.info("Payment cancellation successful. Method: {}, Total Price: {}",
                requestDto.getPaymentMethod(), requestDto.getOrderTotalPrice());
        return result;
    }
}
