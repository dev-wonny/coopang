package com.coopang.order.application.service;

import com.coopang.apicommunication.kafka.message.*;
import com.coopang.apidata.application.payment.enums.PaymentMethodEnum;
import com.coopang.apidata.application.payment.enums.PaymentStatusEnum;
import com.coopang.order.domain.entity.payment.PaymentEntity;
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

    private final PaymentRepository paymentRepository;
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

    @KafkaListener(topics = "complete_product", groupId = "my-group")
    public void createPayment(String message) {
        try {
            ProcessPayment paymentInfo = objectMapper.readValue(message, ProcessPayment.class);
            final String request = tryPayToPG(PaymentMethodEnum.CARD, paymentInfo.getOrderTotalPrice());
            // 만약 결제 성공했을때
            if (request.equals("Payment processed successfully")) {
                paymentDomainService.createPayment(paymentInfo, PaymentStatusEnum.COMPLETED);
                final String sendMessage = objectMapper.writeValueAsString(paymentInfo);
                kafkaTemplate.send("compelete_payment", sendMessage);
            } else { // 결제 실패하거나 예외문에 걸렸을 경우
                paymentDomainService.createPayment(paymentInfo,PaymentStatusEnum.FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 처리
        }
    }
    // 결제 취소 주문 취소에서 이어지는..
    @KafkaListener(topics = "cancel_order", groupId = "my-group")
    public void cancelOrder(String message) {
        try {
            CancelOrder cancelOrder = objectMapper.readValue(message,CancelOrder.class);
            // 해당 결제 정보 찾는 findByOrderId 필요
            PaymentEntity paymentEntity = paymentRepository.findByOrderId(cancelOrder.getOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("Payment not found. orderId=" + cancelOrder.getOrderId()));
            final String request = tryPayToPGCancel(PaymentMethodEnum.CARD, cancelOrder.getOrderTotalPrice());
            // 추후에 결제 취소에 관한 부분 처리 필요 (성공/실패)
            if (request.equals("Payment cancelled")) {
                // 결제 취소 성공 했을때 해당 결제 정보 상태값 변경
                paymentEntity.setPaymentStatus(PaymentStatusEnum.CANCELED);
                // 추후 리팩토링 시 save필요

                // product쪽으로 cancelOrder정보를 RollbackProduct로 담아서 그대로 보내기
                RollbackProduct rollbackProduct = new RollbackProduct();
                rollbackProduct.setOrderId(cancelOrder.getOrderId());
                rollbackProduct.setProductId(cancelOrder.getProductId());
                rollbackProduct.setOrderQuantity(cancelOrder.getOrderQuantity());
                rollbackProduct.setOrderTotalPrice(cancelOrder.getOrderTotalPrice());

                final String sendMessageToProduct = objectMapper.writeValueAsString(rollbackProduct);
                kafkaTemplate.send("rollback_product", sendMessageToProduct);

                // delivery쪽으로 orderId 전달해서 취소 보내기(애매한 시간대에 대한 처리 필요할거 같음)
                CancelDelivery cancelDelivery = new CancelDelivery();
                cancelDelivery.setOrderId(cancelOrder.getOrderId());

                final String sendMessageToDelviery = objectMapper.writeValueAsString(cancelDelivery);
                kafkaTemplate.send("cancel_delivery", sendMessageToDelviery);
            } else { // 결제 취소가 실패 했을경우
                // order쪽으로 결제 취소가 실패 했다는 메세지 보내기
                ErrorCancelOrder errorCancelOrder = new ErrorCancelOrder();
                errorCancelOrder.setOrderId(cancelOrder.getOrderId());
                errorCancelOrder.setErrorMessage(request);

                final String sendMessageToOrder = objectMapper.writeValueAsString(errorCancelOrder);
                kafkaTemplate.send("error_cancel_order", sendMessageToOrder);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // PG사에 결제 요청 처리
    public String tryPayToPG(PaymentMethodEnum paymentMethod, BigDecimal orderTotalPrice) {

        PGRequestDto pgRequestDto = new PGRequestDto();
        pgRequestDto.setPaymentMethod(paymentMethod.toString());
        pgRequestDto.setOrderTotalPrice(orderTotalPrice);
        final String paymentUrl = "http://localhost:19097/payments/v1/pg";

        try{
            ResponseEntity<String> response = restTemplate.postForEntity(paymentUrl, pgRequestDto, String.class);
            return response.getBody();
        }catch (Exception e){
            e.printStackTrace();
            return "Payment request creation failed";
        }
    }
    // 결제 취소에 관한 메서드 작업 해야함
    public String tryPayToPGCancel(PaymentMethodEnum paymentMethod, BigDecimal orderTotalPrice) {

        PGRequestDto pgRequestDto = new PGRequestDto();
        pgRequestDto.setPaymentMethod(paymentMethod.toString());
        pgRequestDto.setOrderTotalPrice(orderTotalPrice);
        final String paymentUrl = "http://localhost:19097/payments/v1/pg/cancel";

        try{
            ResponseEntity<String> response = restTemplate.postForEntity(paymentUrl, pgRequestDto, String.class);
            return response.getBody();
        }catch (Exception e){
            e.printStackTrace();
            return "Cancel payment request creation failed";
        }
    }


}
