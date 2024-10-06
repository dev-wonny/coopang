package com.coopang.order.presentation.controller;


import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.order.application.request.OrderDto;
import com.coopang.order.application.response.OrderCheckResponseDto;
import com.coopang.order.application.response.OrderResponseDto;
import com.coopang.order.application.service.OrderService;
import com.coopang.order.presentation.request.OrderRequestDto;
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

    public OrderController(ModelMapperConfig mapperConfig, OrderService orderService) {
        this.mapperConfig = mapperConfig;
        this.orderService = orderService;
    }

    @PostMapping("/order")
    @Secured("ROLE_MASTER")
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        OrderDto orderDto = mapperConfig.strictMapper().map(orderRequestDto, OrderDto.class);
        OrderResponseDto orderResponseDto = orderService.createOrder(orderDto);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderCheckResponseDto> getOrder(@PathVariable("orderId") UUID orderId) {
        OrderCheckResponseDto orderCheckResponseDto = orderService.checkOrder(orderId);
        return new ResponseEntity<>(orderCheckResponseDto, HttpStatus.OK);
    }

}
