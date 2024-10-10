package com.coopang.order.infrastructure.messaging.order;

import com.coopang.apicommunication.kafka.message.CompleteProduct;
import com.coopang.apicommunication.kafka.message.ErrorProduct;
import com.coopang.apidata.application.order.enums.OrderStatusEnum;
import com.coopang.order.application.service.order.OrderService;
import com.coopang.order.domain.service.order.OrderDomainService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j(topic = "OrderKafkaConsumer")
@Component
public class OrderKafkaConsumer {

    private final ObjectMapper objectMapper;
    private final OrderService orderService;
    private final OrderDomainService orderDomainService;

    public OrderKafkaConsumer(
            ObjectMapper objectMapper,
            OrderService orderService,
            OrderDomainService orderDomainService
    ) {
        this.objectMapper = objectMapper;
        this.orderService = orderService;
        this.orderDomainService = orderDomainService;
    }

    // complete_product 수신
    @KafkaListener(topics = "complete_product", groupId = "order_server")
    public void listenCompleteProduct(String message) {
        try {
            CompleteProduct completeProduct = objectMapper.readValue(message, CompleteProduct.class);
            log.info("Product complete received: {}", completeProduct.getOrderId());

            // 성공에 대한 상태 값 변경
            orderService.updateOrderStatus(completeProduct.getOrderId(), OrderStatusEnum.PENDING);

            // 배송 쪽으로 전달
            orderDomainService.sendProcessDelivery(completeProduct.getOrderId());

        } catch (Exception e) {
            log.error("Error while processing listenCompleteProduct: {}", e.getMessage());
            throw new RuntimeException(e);// 예외 처리
        }
    }


    // error_product 수신
    @KafkaListener(topics = "error_product", groupId = "order_server")
    public void listenProductError(String message) {
        try {
            ErrorProduct productError = objectMapper.readValue(message, ErrorProduct.class);
            log.info("Product error received: {}", productError.getErrorMessage());

            orderService.updateOrderStatus(productError.getOrderId(), OrderStatusEnum.CANCELED);

            orderDomainService.sendProcessPayment(productError.getOrderId(), "CANCEL");

        } catch (Exception e) {
            log.error("Error while processing listenProductError: {}", e.getMessage());
            throw new RuntimeException(e);// 예외 처리
        }
    }
}
