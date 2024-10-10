package com.coopang.order.infrastructure.messaging.payment;

import com.coopang.apicommunication.kafka.message.ProcessPayment;
import com.coopang.apidata.application.payment.enums.PaymentStatusEnum;
import com.coopang.order.domain.service.payment.PaymentDomainService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j(topic = "PaymentKafkaConsumer")
@Component
public class PaymentKafkaConsumer {

    private final ObjectMapper objectMapper;
    private final PaymentDomainService paymentDomainService;

    public PaymentKafkaConsumer(
            ObjectMapper objectMapper,
            PaymentDomainService paymentDomainService
    ) {
        this.objectMapper = objectMapper;
        this.paymentDomainService = paymentDomainService;
    }

    @KafkaListener(topics = "process_payment", groupId = "order_server")
    public void listenProcessPayment(String message) {
        try {
            ProcessPayment processPayment = objectMapper.readValue(message, ProcessPayment.class);
            if (processPayment.getMessage().equals("SUCCESS")){
                // 결제했던 정보 생성
                paymentDomainService.createPayment(processPayment, PaymentStatusEnum.COMPLETED);
            } else {
                // 결제 취소 요청
                paymentDomainService.tryPayPGCancel(processPayment.getOrderTotalPrice());
                // 결제했던 정보 생성
                paymentDomainService.createPayment(processPayment, PaymentStatusEnum.CANCELED);
            }

        } catch (Exception e){
            log.error("Error while processing listenProcessPayment: {}", e.getMessage());
        }
    }
}
