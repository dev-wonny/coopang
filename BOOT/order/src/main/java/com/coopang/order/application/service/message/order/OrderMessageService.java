package com.coopang.order.application.service.message.order;

import com.coopang.apicommunication.kafka.consumer.MessageService;
import com.coopang.apicommunication.kafka.message.*;
import com.coopang.apicommunication.kafka.producer.MessageProducer;
import com.coopang.apidata.application.order.enums.OrderStatusEnum;
import com.coopang.order.application.response.order.OrderResponseDto;
import com.coopang.order.application.service.order.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j(topic = "OrderMessageService")
@Service
@Transactional
public class OrderMessageService implements MessageService {
    /*
    * sendMessage
     주문 등록 후 상품에게 전달 (process_product) - OK
     배송쪽에 정보 전달 (process_delivery) - OK
     결제 성공 후 정보 전달 (complete_payment) - OK
     결제 취소 정보 전달 (cancel_payment) - OK
     배송 취소 정보 전달 (cancel_delivery) - OK
     상품 롤백 정보 전달 (rollback_product) - OK
    * processMessage
     재고 감소 성공 수신 (complete_product) - OK
     재고 감소 실패 수신 (error_product) - OK
     배송 상태 변경으로 주문 상태 변경 수신 (process_change_status) - OK
     */
    private final ObjectMapper objectMapper;
    private final OrderService orderService;
    private final MessageProducer messageProducer;

    public OrderMessageService(
           ObjectMapper objectMapper,
            OrderService orderService,
            MessageProducer messageProducer
    ) {
        this.objectMapper = objectMapper;
        this.orderService = orderService;
        this.messageProducer = messageProducer;
    }
    @Override
    public void processMessage(String topic, String message) {
        switch (topic) {
            case "complete_product":
                listenCompleteProduct(message);
                break;
            case "error_product":
                listenErrorProduct(message);
                break;
            case "process_change_status":
                listenProcessChangeStatus(message);
                break;
            default:
                log.warn("Unknown topic: {}", topic);
        }
    }

    private void listenCompleteProduct(String message) {
        try {
            CompleteProduct completeProduct = objectMapper.readValue(message, CompleteProduct.class);
            log.info("Product complete received: {}", completeProduct.getOrderId());
            // 주문 상태 변경
            orderService.updateOrderStatus(completeProduct.getOrderId(), OrderStatusEnum.PENDING);
            // 배송쪽으로 보내기
            sendProcessDelivery(completeProduct.getOrderId());
        } catch (Exception e) {
            log.error("Error while processing listenCompleteProduct: {}", e.getMessage(), e);
            throw new RuntimeException(e); // 예외 처리
        }
    }

    private void listenErrorProduct(String message) {
        try {
            ErrorProduct errorProduct = objectMapper.readValue(message, ErrorProduct.class);
            log.info("Product error received: {}", errorProduct.getErrorMessage());
            orderService.updateOrderStatus(errorProduct.getOrderId(), OrderStatusEnum.CANCELED);
            sendCancelPayment(errorProduct.getOrderId());
        } catch (Exception e) {
            log.error("Error while processing listenProductError: {}", e.getMessage(), e);
            throw new RuntimeException(e); // 예외 처리
        }
    }

    private void listenProcessChangeStatus(String message) {
        try {
            ProcessChangeStatus processChangeStatus = objectMapper.readValue(message, ProcessChangeStatus.class);
            log.info("Change status received: {}", processChangeStatus.getOrderId());
            updateOrderStatusBasedOnDelivery(processChangeStatus);
        } catch (Exception e) {
            log.error("Error while processing listenProcessChangeStatus: {}", e.getMessage(), e);
        }
    }

    private void updateOrderStatusBasedOnDelivery(ProcessChangeStatus processChangeStatus) {
        switch (processChangeStatus.getDeliveryStatus()) {
            case "HUB_DELIVERY_ASSIGNMENT_COMPLETED":
                orderService.updateOrderStatus(processChangeStatus.getOrderId(), OrderStatusEnum.SHIPPED);
                break;
            case "DELIVERY_COMPLETED_TO_CUSTOMER":
                orderService.updateOrderStatus(processChangeStatus.getOrderId(), OrderStatusEnum.DELIVERED);
                break;
            default:
                log.warn("Unknown delivery status: {}", processChangeStatus.getDeliveryStatus());
        }
    }


    /*
    * sendMessage
     주문 등록 후 상품에게 전달 (process_product)
     배송쪽에 정보 전달 (process_delivery)
     결제 성공 후 정보 전달 (complete_payment)
     결제 취소 정보 전달 (cancel_payment)
     배송 취소 정보 전달 (cancel_delivery)
     상품 롤백 정보 전달 (rollback_product)
     */

    /*
    <process_product> 로 전달하기 위한 세팅
     sendProcessProduct
    - 제품 수량 검증을 위한 카프카 토픽
     */
    public void sendProcessProduct(
            UUID orderId,
            UUID productId,
            Integer orderQuantity,
            BigDecimal orderTotalPrice
    ){
        ProcessProduct processProduct = new ProcessProduct();
        processProduct.setOrderId(orderId);
        processProduct.setProductId(productId);
        processProduct.setOrderQuantity(orderQuantity);
        processProduct.setOrderTotalPrice(orderTotalPrice);

        sendMessage("process_product",processProduct);
    }
    /*
    <process_delivery> 로 전달하기 위한 세팅
     sendProcessDelivery
    Delete : 현재는 RequestDto에서 받는걸로 진행 다른 방법으로 수신할 예정
     */
    public void sendProcessDelivery(UUID orderId){
        OrderResponseDto orderResponseDto = orderService.findById(orderId);

        ProcessDelivery processDelivery = new ProcessDelivery();
        processDelivery.setOrderId(orderId);
        processDelivery.setUserId(orderResponseDto.getUserId());
        processDelivery.setNearHubId(orderResponseDto.getNearHubId());
        processDelivery.setProductHubId(orderResponseDto.getProductHubId());
        processDelivery.setZipCode(orderResponseDto.getAddress().getZipCode());
        processDelivery.setAddress1(orderResponseDto.getAddress().getAddress1());
        processDelivery.setAddress2(orderResponseDto.getAddress().getAddress2());

        sendMessage("process_delivery",processDelivery);
    }
    /*
    <complete_payment> 로 전달하기 위한 세팅
     sendCompletePayment
    - 결제 기록 등록을 위한 카프카 토픽
     */
    public void sendCompletePayment(
            UUID orderId,
            UUID pgPaymentId,
            BigDecimal orderTotalPrice,
            String paymentMethod
    ){

        CompletePayment completePayment = new CompletePayment();
        completePayment.setOrderId(orderId);
        completePayment.setPgPaymentId(pgPaymentId);
        completePayment.setOrderTotalPrice(orderTotalPrice);
        completePayment.setPaymentMethod(paymentMethod);

        sendMessage("complete_payment",completePayment);
    }
    /*
    <cancel_payment> 로 전달하기 위한 세팅
     sendCancelPayment
    - 결제 취소을 위한 카프카 토픽
     */
    public void sendCancelPayment(UUID orderId)
    {
        CancelPayment cancelPayment = new CancelPayment();
        cancelPayment.setOrderId(orderId);

        sendMessage("cancel_payment",cancelPayment);
    }
    /*
    <cancel_delivery> 로 전달하기 위한 세팅
     sendCancelDelivery
    - 결제 기록 등록을 위한 카프카 토픽
     */
    public void sendCancelDelivery(UUID orderId)
    {
        CancelDelivery cancelDelivery = new CancelDelivery();
        cancelDelivery.setOrderId(orderId);

        sendMessage("cancel_delivery",cancelDelivery);
    }
    /*
    <rollback_product> 로 전달하기 위한 세팅
     sendRollbackProduct
    - 상품 롤백을 위한 카프카 토픽
    * 사용처
    - 주문 취소 진행시에만 사용
     */
    public void sendRollbackProduct(UUID orderId){
        OrderResponseDto orderResponseDto = orderService.findById(orderId);

        RollbackProduct rollbackProduct = new RollbackProduct();
        rollbackProduct.setOrderId(orderResponseDto.getOrderId());
        rollbackProduct.setProductId(orderResponseDto.getProductId());
        rollbackProduct.setOrderQuantity(orderResponseDto.getOrderQuantity());
        rollbackProduct.setOrderTotalPrice(orderResponseDto.getOrderTotalPrice());

        sendMessage("rollback_product",rollbackProduct);
    }

    /*
    공통 메서드
    1. 메세지 보내기
     */

    //1. 메세지 보내기
    private void sendMessage(String topic, Object payload) {
        try {
            final String message = objectMapper.writeValueAsString(payload);
            messageProducer.sendMessage(topic, message);
        } catch (Exception e) {
            log.error("Error while sending message to topic {}: {}", topic, e.getMessage(), e);
        }
    }
}
