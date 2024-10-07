package com.coopang.order.application.service;

import com.coopang.order.application.*;
import com.coopang.order.application.request.OrderDto;
import com.coopang.order.application.response.OrderCancelResponseDto;
import com.coopang.order.application.response.OrderCheckResponseDto;
import com.coopang.order.application.response.OrderResponseDto;
import com.coopang.order.domain.OrderStatusEnum;
import com.coopang.order.domain.entity.order.OrderEntity;
import com.coopang.order.domain.repository.OrderRepository;
import com.coopang.order.domain.service.OrderDomainService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

@Slf4j(topic = "OrderService")
@Service
@Transactional
public class OrderService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OrderRepository orderRepository;
    private final OrderDomainService orderDomainService;
    private final ObjectMapper objectMapper;

    // 응답을 저장할 맵과 대기용 CountDownLatch
    private final ConcurrentHashMap<UUID, CountDownLatch> latchMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, OrderCheckResponseDto> responseMap = new ConcurrentHashMap<>();

    public OrderService(OrderRepository orderRepository,
                        OrderDomainService orderDomainService,
                        ObjectMapper objectMapper,
                        KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.orderDomainService = orderDomainService;
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    // 주문 등록 기본적인 정보
    public OrderResponseDto createOrder(OrderDto orderDto) {
        OrderEntity orderEntity = orderDomainService.createOrder(orderDto);
        log.info("Order created successfully: {}", orderEntity);
        return OrderResponseDto.fromOrder(orderEntity);
    }

    // 주문하기 -> 주문 서비스로 전달
    public OrderCheckResponseDto checkOrder(UUID orderId) {
        OrderEntity orderEntity = findByOrderId(orderId);

        ProcessProduct processProduct = new ProcessProduct();
        processProduct.setOrderId(orderId);
        processProduct.setProductId(orderEntity.getProductId());
        processProduct.setOrderQuantity(orderEntity.getOrderQuantity());
        processProduct.setOrderTotalPrice(orderEntity.getOrderTotalPrice());

        try {
            // 주문 요청 메시지 전송
            String message = objectMapper.writeValueAsString(processProduct);
            kafkaTemplate.send("process_product", message);
            log.info("Order processing message sent: {}", message);
        } catch (JsonProcessingException e) {
            log.error("Error while converting ProcessProduct to JSON: {}", e.getMessage());
            e.printStackTrace(); // 예외 처리
        }

        return waitForResponse(orderId); // 응답 대기
    }

    // 주문취소 -> 주문 상태 (Ready 나 Pending 상태에만 가능)
    public String cancelOrder(UUID orderId) {
        OrderEntity orderEntity = findByOrderId(orderId);
        if (orderEntity.getOrderStatus().equals(OrderStatusEnum.READY) ||
                orderEntity.getOrderStatus().equals(OrderStatusEnum.PENDING)) {

            CancelOrder cancelOrder = new CancelOrder();
            cancelOrder.setOrderId(orderId);
            cancelOrder.setProductId(orderEntity.getProductId());
            cancelOrder.setOrderQuantity(orderEntity.getOrderQuantity());
            cancelOrder.setOrderTotalPrice(orderEntity.getOrderTotalPrice());

            try {
                String message = objectMapper.writeValueAsString(cancelOrder);
                kafkaTemplate.send("cancel_order", message);
                log.info("Cancel Order processing message sent: {}", message);
            } catch (JsonProcessingException e) {
                log.error("Error while converting CancelOrder to JSON: {}", e.getMessage(), e);
            }

            orderEntity.setOrderStatus(OrderStatusEnum.CANCELED);
            log.info("Order ID {} has been canceled successfully.", orderId);
            return "주문 취소가 정상적으로 등록되었습니다.";
        } else {
            log.warn("Order ID {} cannot be canceled as it is already in progress.", orderId);
            return "해당 주문건은 이미 배송이 진행이 되어서 취소가 안됩니다.";
        }
    }

    // 응답을 기다리는 메서드
    private OrderCheckResponseDto waitForResponse(UUID orderId) {
        CountDownLatch latch = new CountDownLatch(1);
        latchMap.put(orderId, latch); // 주문 ID에 대한 latch 저장

        try {
            latch.await(); // 응답 대기
            log.info("Response received for Order ID: {}", orderId);
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for response: {}", e.getMessage());
            e.printStackTrace(); // 예외 처리
        }

        return responseMap.remove(orderId); // 응답 반환 후 맵에서 제거
    }

    // product_error 수신용
    @KafkaListener(topics = "product_error", groupId = "my-group")
    public void listenProductError(String message) {
        try {
            ProductError productError = objectMapper.readValue(message, ProductError.class);
            log.error("Product error received: {}", productError.getErrorMessage());

            // 응답 메시지 생성
            MessageToOrder response = new MessageToOrder();
            response.setOrderId(productError.getOrderId());
            response.setMessage(productError.getErrorMessage());

            // 응답을 응답 토픽에 전송
            String responseMessage = objectMapper.writeValueAsString(response);
            kafkaTemplate.send("message_to_order", responseMessage);
            log.info("Product error response sent: {}", responseMessage);
        } catch (Exception e) {
            log.error("Error while processing product error message: {}", e.getMessage());
            e.printStackTrace(); // 예외 처리
        }
    }

    // payment_error 수신용
    @KafkaListener(topics = "payment_error", groupId = "my-group")
    public void listenPaymentError(String message) {
        try {
            PaymentError paymentError = objectMapper.readValue(message, PaymentError.class);
            log.error("Payment error received: {}", paymentError.getErrorMessage());

            // 응답 메시지 생성
            MessageToOrder response = new MessageToOrder();
            response.setOrderId(paymentError.getOrderId());
            response.setMessage(paymentError.getErrorMessage());

            // 응답을 응답 토픽에 전송
            String responseMessage = objectMapper.writeValueAsString(response);
            kafkaTemplate.send("message_to_order", responseMessage);
            log.info("Payment error response sent: {}", responseMessage);
        } catch (Exception e) {
            log.error("Error while processing payment error message: {}", e.getMessage());
            e.printStackTrace(); // 예외 처리
        }
    }

    // complete_payment 수신용
    @KafkaListener(topics = "complete_payment", groupId = "my-group")
    public void listenCompletePayment(String message) {
        try {
            CompletePayment completePayment = objectMapper.readValue(message, CompletePayment.class);
            log.info("Complete payment received for Order ID: {}", completePayment.getOrderId());

            // 응답 메시지 생성
            MessageToOrder response = new MessageToOrder();
            response.setOrderId(completePayment.getOrderId());
            response.setMessage(completePayment.getCompleteMessage());

            // 응답을 응답 토픽에 전송
            String responseMessage = objectMapper.writeValueAsString(response);
            kafkaTemplate.send("message_to_order", responseMessage);
            log.info("Complete payment response sent: {}", responseMessage);

            // 주문 Update 진행
            OrderEntity orderEntity = findByOrderId(completePayment.getOrderId());
            orderEntity.updateAtOrder(completePayment.getCompanyId());

            // 배송쪽 전달용 메세지 세팅
            ProcessDelivery processDelivery = orderDomainService.processDelivery(
                    orderEntity.getOrderId(),
                    orderEntity.getUserId(),
                    orderEntity.getCompanyId(),
                    orderEntity.getAddressEntity().getZipCode(),
                    orderEntity.getAddressEntity().getAddress1(),
                    orderEntity.getAddressEntity().getAddress2()
            );

            // 배송쪽 토픽으로 메세지 전달
            try {
                String processDeliveryMessage = objectMapper.writeValueAsString(processDelivery);
                kafkaTemplate.send("process_delivery", processDeliveryMessage);
                log.info("Delivery processing message sent: {}", processDeliveryMessage);
            } catch (JsonProcessingException e) {
                log.error("Error while converting CancelOrder to JSON: {}", e.getMessage());
                e.printStackTrace(); // 예외 처리
            }

        } catch (Exception e) {
            log.error("Error while processing complete payment message: {}", e.getMessage());
            e.printStackTrace(); // 예외 처리
        }
    }

    // complete_order 수신용
    @KafkaListener(topics = "complete_delivery", groupId = "my-group")
    public void listenCompleteDelivery(String message) {
        try {
            CompleteDelivery response = objectMapper.readValue(message, CompleteDelivery.class);
            log.info("Complete delivery received for Order ID: {}", response.getOrderId());

            // 업데이트 진행
            OrderEntity orderEntity = findByOrderId(response.getOrderId());
            orderEntity.updateAtCompleteDelivery(
                    response.getHubId(),
                    response.getNearHubId()
            );

        } catch (Exception e) {
            log.error("Error while processing complete_delivery error message: {}", e.getMessage());
            e.printStackTrace(); // 예외 처리
        }
    }

    // 응답을 수신하는 메서드
    @KafkaListener(topics = "message_to_order", groupId = "my-group")
    public void listenMessageToOrder(String message) {
        try {
            OrderCheckResponseDto response = objectMapper.readValue(message, OrderCheckResponseDto.class);
            log.info("Response received for Order ID: {}", response.getOrderId());

            // 응답 저장 및 latch 카운트다운
            responseMap.put(response.getOrderId(), response);
            CountDownLatch latch = latchMap.remove(response.getOrderId());
            if (latch != null) {
                latch.countDown(); // 응답 수신 시 카운트다운
            }
        } catch (Exception e) {
            log.error("Error while processing message to order: {}", e.getMessage());
            e.printStackTrace(); // 예외 처리
        }
    }

    private OrderEntity findByOrderId(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found. orderId=" + orderId));
    }


}
