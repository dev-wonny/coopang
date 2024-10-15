package com.coopang.order.domain.service.payment;

import com.coopang.apicommunication.kafka.message.ProcessPayment;
import com.coopang.apidata.application.payment.enums.PaymentStatusEnum;
import com.coopang.order.domain.entity.payment.PaymentEntity;
import com.coopang.order.infrastructure.repository.payment.PaymentJpaRepository;
import com.coopang.order.presentation.request.pg.PGRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Slf4j(topic = "PaymentDomainService")
@Service
@Transactional
public class PaymentDomainService {

    private final PaymentJpaRepository paymentJpaRepository;
    private final RestTemplate restTemplate;

    public PaymentDomainService(
            PaymentJpaRepository paymentJpaRepository,
            RestTemplate restTemplate
    ) {
        this.paymentJpaRepository = paymentJpaRepository;
        this.restTemplate = restTemplate;
    }

    public void createPayment(ProcessPayment paymentProcessDto, PaymentStatusEnum paymentStatus ){
        PaymentEntity paymentEntity = PaymentEntity.create(
                paymentProcessDto.getOrderId(),
                paymentProcessDto.getOrderTotalPrice(),
                paymentStatus
        );
        paymentJpaRepository.save(paymentEntity);
    }

    // 결제 취소
    public String tryPayPGCancel(BigDecimal orderTotalPrice) {

        PGRequestDto pgRequestDto = new PGRequestDto();
        pgRequestDto.setOrderTotalPrice(orderTotalPrice);
        final String paymentUrl = "http://localhost:19097/payments/v1/pg/cancel";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-User-Id","1111-1111-1111-1111");
        headers.set("X-User-Role","MASTER");

        HttpEntity<PGRequestDto> requestEntity = new HttpEntity<>(pgRequestDto, headers);

        try{
            ResponseEntity<String> response = restTemplate.postForEntity(paymentUrl, requestEntity, String.class);
            return response.getBody();
        }catch (Exception e){
            log.error("Error while processing tryPayPGCancel: {}", e.getMessage());
            return "Cancel payment request creation failed";
        }
    }
}
