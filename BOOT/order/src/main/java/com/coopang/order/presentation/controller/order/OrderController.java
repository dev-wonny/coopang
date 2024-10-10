package com.coopang.order.presentation.controller.order;


import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.apidata.application.order.enums.OrderStatusEnum;
import com.coopang.order.application.request.order.OrderDto;
import com.coopang.order.application.response.order.OrderResponseDto;
import com.coopang.order.application.service.order.OrderService;
import com.coopang.order.domain.service.order.OrderDomainService;
import com.coopang.order.presentation.request.order.OrderRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "OrderContorller API", description = "OrderContorller API")
@Slf4j(topic = "OrderContorller")
@RestController
@RequestMapping("/orders/v1")
public class OrderController {

    private final ModelMapperConfig mapperConfig;
    private final OrderService orderService;
    private final OrderDomainService orderDomainService;

    public OrderController(ModelMapperConfig mapperConfig, OrderService orderService, OrderDomainService orderDomainService) {
        this.mapperConfig = mapperConfig;
        this.orderService = orderService;
        this.orderDomainService = orderDomainService;
    }

    // 주문 생성 //
    @PostMapping("/order")
    @Secured("ROLE_MASTER")
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        OrderDto orderDto = mapperConfig.strictMapper().map(orderRequestDto, OrderDto.class);
        OrderResponseDto orderResponseDto = orderService.createOrder(orderDto);

        // 결제 기록 요청
        orderDomainService.sendProcessPayment(orderResponseDto.getOrderId(),"SUCCESS");

        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

    // 주문취소
    // 주문 상태 값 변경 요청
    @PatchMapping("/order/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable("orderId") UUID orderId) {
        orderService.updateOrderStatus(orderId, OrderStatusEnum.CANCELED);
        final String message = "Order Cancelled";

        // 결제 취소 보내기
        orderDomainService.sendProcessPayment(orderId,"CANCEL");

        // 상품 롤백 보내기
        orderDomainService.sendRollbackProduct(orderId);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }



}
