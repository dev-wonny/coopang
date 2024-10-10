package com.coopang.order.application.response.order;


import com.coopang.order.domain.entity.order.OrderEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OrderCheckResponseDto {
    private UUID orderId;
    private String message;

    private OrderCheckResponseDto(
            UUID orderId,
            String message
    ){
        this.orderId = orderId;
        this.message = message;
    }

    public static OrderCheckResponseDto fromOrderCheck(
            OrderEntity orderEntity,
            String message
    ){
        return new OrderCheckResponseDto(
                orderEntity.getOrderId(),
                message
        );
    }
}
