package com.coopang.order.application.service;

import com.coopang.order.application.PaymentProcessDto;
import com.coopang.order.application.ProcessProduct;
import com.coopang.order.application.response.OrderCheckResponseDto;
import com.coopang.order.domain.PaymentMethodEnum;
import com.coopang.order.domain.PaymentStatusEnum;
import com.coopang.order.domain.repository.PaymentRepository;
import com.coopang.order.domain.service.PaymentDomainService;
import com.coopang.order.presentation.request.PGRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Slf4j(topic = "PaymentService")
@Service
@Transactional
public class PaymentService {

    private PaymentRepository paymentRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final PaymentDomainService paymentDomainService;


    public PaymentService(
            KafkaTemplate<String, String> kafkaTemplate,
            PaymentRepository paymentRepository,
            ObjectMapper objectMapper,
            RestTemplate restTemplate,
            PaymentDomainService paymentDomainService
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.paymentRepository = paymentRepository;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.paymentDomainService = paymentDomainService;
    }

    @KafkaListener(topics = "process_payment")
    public void createPayment(String message) {
        try {
            PaymentProcessDto paymentInfo = objectMapper.readValue(message, PaymentProcessDto.class);
            final String request = tryPayToPG(PaymentMethodEnum.CARD, paymentInfo.getOrderTotalPrice());
            // 만약 결제 성공했을때
            if (request.equals("Payment processed successfully")) {
                paymentDomainService.createPayment(paymentInfo,PaymentStatusEnum.COMPLETED);
                final String sendMessage = objectMapper.writeValueAsString(paymentInfo);
                kafkaTemplate.send("compelete_payment", sendMessage);
            } else { // 결제 실패하거나 예외문에 걸렸을 경우
                paymentDomainService.createPayment(paymentInfo,PaymentStatusEnum.FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 처리
        }
    }

    // PG사에 결제 요청 처리
    public String tryPayToPG(PaymentMethodEnum paymentMethod, BigDecimal orderTotalPrice) {

        PGRequestDto pgRequestDto = new PGRequestDto();
        pgRequestDto.setPaymentMethod(paymentMethod.toString());
        pgRequestDto.setOrderTotalPrice(orderTotalPrice);
        final String paymentUrl = "http://localhost:19097/pay/PG";

        try{
            ResponseEntity<String> response = restTemplate.postForEntity(paymentUrl, pgRequestDto, String.class);
            return response.getBody();
        }catch (Exception e){
            e.printStackTrace();
            return "Payment request creation failed";
        }
    }

    // Kafka 리스너 추후에 Payment쪽으로 이동
    @KafkaListener(topics = "delivery-topic", groupId = "my-group")
    public void listen(String message) {
        try {
            ProcessProduct orderInfo = objectMapper.readValue(message, ProcessProduct.class);
            String successMessage = handleOrder(orderInfo); // 주문 처리 후 성공 메시지 반환

            // 응답 메시지 생성
            OrderCheckResponseDto response = new OrderCheckResponseDto();
            response.setOrderId(orderInfo.getOrderId());
            response.setMessage(successMessage);

            // 응답을 응답 토픽에 전송
            String responseMessage = objectMapper.writeValueAsString(response);
            kafkaTemplate.send("delivery-response-topic", responseMessage);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 처리
        }
    }
    private String handleOrder(ProcessProduct orderInfo) {
        // 주문 정보 처리 로직
        System.out.println("Received Order Info: ID=" + orderInfo.getOrderId() +
                ", Quantity=" + orderInfo.getOrderQuantity() +
                ", Price=" + orderInfo.getOrderTotalPrice());

        // 주문 처리 성공 메시지 반환
        return "Order processed successfully";
    }
}
