package com.coopang.order.application.service.common;

import com.coopang.apicommunication.kafka.message.*;
import com.coopang.apidata.application.order.enums.OrderStatusEnum;
import com.coopang.order.application.service.order.OrderService;
import com.coopang.order.domain.entity.order.OrderEntity;
import com.coopang.order.domain.repository.order.OrderRepository;
import com.coopang.order.infrastructure.message.common.MessageSender;
import com.coopang.order.infrastructure.message.kafka.order.OrderKafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j(topic = "OrderKafkaService")
@Service
@Transactional
public class OrderMessageServiceCopy implements MessageService {

    private final OrderKafkaProducer orderKafkaProducer;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    public OrderMessageServiceCopy(
            OrderKafkaProducer orderKafkaProducer,
            OrderRepository orderRepository,
            ObjectMapper objectMapper,
            OrderService orderService) {
        this.orderKafkaProducer = orderKafkaProducer;
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
        this.orderService = orderService;
    }
    /*
        common
         */
    @Override
    public void listen(String topic, String message) {
        if (topic.equals("complete_product")) {
            try {
                CompleteProduct completeProduct = objectMapper.readValue(message, CompleteProduct.class);
                log.info("Product complete received: {}", completeProduct.getOrderId());
                // 성공에 대한 상태 값 변경
                orderService.updateOrderStatus(completeProduct.getOrderId(), OrderStatusEnum.PENDING);
            } catch (Exception e) {
                log.error("Error while processing listenCompleteProduct: {}", e.getMessage());
                throw new RuntimeException(e);// 예외 처리
            }
        } else if (topic.equals("error_product")) {
            try {
                ErrorProduct productError = objectMapper.readValue(message, ErrorProduct.class);
                log.info("Product error received: {}", productError.getErrorMessage());
                // Todo : 주문 상태 변경 및 결제 취소 요청
            } catch (Exception e) {
                log.error("Error while processing listenProductError: {}", e.getMessage());
                throw new RuntimeException(e);// 예외 처
            }
        }
    }

    /*
    <process_product> 로 전달하기 위한 세팅
    Todo : sendProcessProduct
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
        try {
            final String message = objectMapper.writeValueAsString(processProduct);
            orderKafkaProducer.sendProcessProduct(message);
        } catch (Exception e){
            log.error("Error while sendProcessProduct: {}", e.getMessage());
        }
    }


    /*
    <process_delivery> 로 전달하기 위한 세팅
    Todo : sendProcessDelivery
    Delete : 현재는 RequestDto에서 받는걸로 진행 다른 방법으로 수신할 예정
     */

    /*
    <process_payment> 로 전달하기 위한 세팅
    Todo : sendProcessPayment
    - 결제 기록 등록을 위한 카프카 토픽
     */
    public void sendProcessPayment(UUID orderId, String message){
        OrderEntity orderEntity = findByOrderId(orderId);

        ProcessPayment processPayment = new ProcessPayment();
        processPayment.setOrderId(orderEntity.getOrderId());
        processPayment.setOrderTotalPrice(orderEntity.getOrderTotalPrice());
        processPayment.setMessage(message);

        orderKafkaProducer.sendProcessPayment(processPayment);
    }

    /*
    <rollback_product> 로 전달하기 위한 세팅
    Todo : sendRollbackProduct
    - 상품 롤백을 위한 카프카 토픽
    * 사용처
    - 주문 취소 진행시에만 사용
     */
    public void sendRollbackProduct(UUID orderId){
        OrderEntity orderEntity = findByOrderId(orderId);

        RollbackProduct rollbackProduct = new RollbackProduct();
        rollbackProduct.setOrderId(orderEntity.getOrderId());
        rollbackProduct.setProductId(orderEntity.getProductId());
        rollbackProduct.setOrderQuantity(orderEntity.getOrderQuantity());
        rollbackProduct.setOrderTotalPrice(orderEntity.getOrderTotalPrice());

        orderKafkaProducer.sendRollbackProduct(rollbackProduct);
    }

    /*
    공통 메서드
    Todo : id값으로 정보 찾아오기
     */
    private OrderEntity findByOrderId(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found. orderId=" + orderId));
    }

}
