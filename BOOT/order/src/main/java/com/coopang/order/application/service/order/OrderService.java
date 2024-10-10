package com.coopang.order.application.service.order;

import com.coopang.apicommunication.kafka.message.*;
import com.coopang.apidata.application.order.enums.OrderStatusEnum;
import com.coopang.order.application.request.order.OrderDto;
import com.coopang.order.application.response.order.OrderCheckResponseDto;
import com.coopang.order.application.response.order.OrderResponseDto;
import com.coopang.order.domain.entity.order.OrderEntity;
import com.coopang.order.domain.repository.order.OrderRepository;
import com.coopang.order.domain.service.order.OrderDomainService;
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

    private final OrderRepository orderRepository;
    private final OrderDomainService orderDomainService;

    public OrderService(OrderRepository orderRepository,
                        OrderDomainService orderDomainService
    ) {
        this.orderRepository = orderRepository;
        this.orderDomainService = orderDomainService;
    }

    // 주문 등록
    public OrderResponseDto createOrder(OrderDto orderDto) {
        // 주문 등록
        OrderEntity orderEntity = orderDomainService.createOrder(orderDto);
        log.info("Order created successfully: {}", orderEntity);
        // process_product 토픽으로 메세지 보내기
        orderDomainService.sendProcessProduct(orderEntity.getOrderId());
        log.info("Send process_product successfully: {}", orderEntity);
        return OrderResponseDto.fromOrder(orderEntity);
    }

    // 주문 상태 update
    public void updateOrderStatus(UUID orderId, OrderStatusEnum orderStatusEnum) {
        OrderEntity orderEntity = findByOrderId(orderId);
        orderEntity.setOrderStatus(orderStatusEnum);
    }

    // 공통 메서드
    private OrderEntity findByOrderId(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found. orderId=" + orderId));
    }


}
