package com.coopang.order.infrastructure.message.common;

import com.coopang.apicommunication.kafka.message.CompleteDelivery;
import com.coopang.apicommunication.kafka.message.CompleteProduct;
import com.coopang.apicommunication.kafka.message.ErrorProduct;
import com.coopang.apidata.application.order.enums.OrderStatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j(topic = "OrderKafkaConsumer")
@Component
public class OrderKafkaConsumerCopy {

    private final ObjectMapper objectMapper;

    public OrderKafkaConsumerCopy(
            ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;
    }

    // complete_product 수신
    @KafkaListener(topics = "complete_product", groupId = "order_server")
    public void listenCompleteProduct(String message) {
        try {
            CompleteProduct completeProduct = objectMapper.readValue(message, CompleteProduct.class);
            log.info("Product complete received: {}", completeProduct.getOrderId());
            // 성공에 대한 상태 값 변경
            iOrderService.updateOrderStatus(completeProduct.getOrderId(), OrderStatusEnum.PENDING);
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

            iOrderService.updateOrderStatus(productError.getOrderId(), OrderStatusEnum.CANCELED);

            messageSender.sendProcessPayment(productError.getOrderId(), "CANCEL");
        } catch (Exception e) {
            log.error("Error while processing listenProductError: {}", e.getMessage());
            throw new RuntimeException(e);// 예외 처리
        }
    }

    /*
     complete_delivery 수신
     Todo : 주문 상태 업데이트
     */
    @KafkaListener(topics = "complete_delivery", groupId = "order_server")
    public void listenCompleteDelivery(String message) {
        try {
            CompleteDelivery completeDelivery = objectMapper.readValue(message, CompleteDelivery.class);
            log.info("Delivery complete received: {}", completeDelivery.getOrderId());
            iOrderService.updateOrderInfo(
                    completeDelivery.getOrderId(),
                    completeDelivery.getHubId(),
                    completeDelivery.getNearHubId()
            );

        } catch (Exception e){
            log.error("Error while processing listenCompleteDelivery: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /*
    process_change_status 수신
    Todo : 상태값 수신 마다 주문 상태값 다르게 설정
    수신되는 배송 상태값 : HUB_DELIVERY_ASSIGNMENT_COMPLETED(허브 배송 배차 완료)일 경우 -> 주문 상태값 SHIPPED로 변경
    수신되는 배송 상태값 : DELIVERY_COMPLETED_TO_CUSTOMER(고객 배송 도착)일 경우 -> 주문 상태값 DELIVERYED
     */
    @KafkaListener(topics = "process_change_status", groupId = "order_server")
    public void listenProcessChangeStatus(String message) {
//        try {
//            ProcessChangeStatus processChangeStatus = objectMapper.readValue(message, ProcessChangeStatus.class);
//            log.info("Change status received: {}", processChangeStatus.getOrderId());
//            if (processChangeStatus.getDeliveryStatus().equals("HUB_DELIVERY_ASSIGNMENT_COMPLETED")){
//                orderService.updateOrderStatus(processChangeStatus.getOrderId(), OrderStatusEnum.SHIPPED);
//            } else if (processChangeStatus.getDeliveryStatus().equals("DELIVERY_COMPLETED_TO_CUSTOMER")) {
//                orderService.updateOrderStatus(processChangeStatus.getOrderId(), OrderStatusEnum.DELIVERED);
//            }

//        } catch (Exception e) {
//            log.error("Error while processing listenProcessChangeStatus: {}", e.getMessage());
//        }
    }
}
