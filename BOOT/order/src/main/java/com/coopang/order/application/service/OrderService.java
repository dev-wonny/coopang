package com.coopang.order.application.service;

import com.coopang.order.application.OrderTestDto;
import com.coopang.order.application.request.OrderDto;
import com.coopang.order.application.response.OrderCheckResponseDto;
import com.coopang.order.application.response.OrderResponseDto;
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
        return OrderResponseDto.fromOrder(orderEntity);
    }

    // 주문하기 -> 주문 서비스로 전달
    public OrderCheckResponseDto checkOrder(UUID orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow();

        OrderTestDto orderInfo = new OrderTestDto();
        orderInfo.setOrderId(orderId);
        orderInfo.setProductId(orderEntity.getProductId());
        orderInfo.setOrderQuantity(orderEntity.getOrderQuantity());
        orderInfo.setOrderTotalPrice(orderEntity.getOrderTotalPrice());

        try {
            // 주문 요청 메시지 전송
            String message = objectMapper.writeValueAsString(orderInfo);
            kafkaTemplate.send("process_product", message);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // 예외 처리
        }

        return waitForResponse(orderId); // 응답 대기
    }

    // 응답을 기다리는 메서드
    private OrderCheckResponseDto waitForResponse(UUID orderId) {
        CountDownLatch latch = new CountDownLatch(1);
        latchMap.put(orderId, latch); // 주문 ID에 대한 latch 저장

        try {
            latch.await(); // 응답 대기
        } catch (InterruptedException e) {
            e.printStackTrace(); // 예외 처리
        }

        return responseMap.remove(orderId); // 응답 반환 후 맵에서 제거
    }

    // Kafka 리스너 추후에 Payment쪽으로 이동
    @KafkaListener(topics = "delivery-topic", groupId = "my-group")
    public void listen(String message) {
        try {
            OrderTestDto orderInfo = objectMapper.readValue(message, OrderTestDto.class);
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

    // 응답을 수신하는 메서드
    @KafkaListener(topics = "delivery-response-topic", groupId = "my-group")
    public void listenResponse(String message) {
        try {
            OrderCheckResponseDto response = objectMapper.readValue(message, OrderCheckResponseDto.class);
            UUID orderId = response.getOrderId(); // 응답에서 주문 ID 추출

            // 응답 저장 및 latch 카운트다운
            responseMap.put(orderId, response);
            CountDownLatch latch = latchMap.remove(orderId);
            if (latch != null) {
                latch.countDown(); // 응답 수신 시 카운트다운
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 처리
        }
    }

    private String handleOrder(OrderTestDto orderInfo) {
        // 주문 정보 처리 로직
        System.out.println("Received Order Info: ID=" + orderInfo.getOrderId() +
                ", Quantity=" + orderInfo.getOrderQuantity() +
                ", Price=" + orderInfo.getOrderTotalPrice());

        // 주문 처리 성공 메시지 반환
        return "Order processed successfully";
    }
}
