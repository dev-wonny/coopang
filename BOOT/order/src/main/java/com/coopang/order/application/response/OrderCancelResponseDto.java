package com.coopang.order.application.response;

import com.coopang.apidata.application.address.Address;
import com.coopang.order.domain.OrderStatusEnum;
import com.coopang.order.domain.entity.order.OrderEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class OrderCancelResponseDto {

    private UUID orderId;
    private UUID productId;
    private Integer orderQuantity;
    private BigDecimal orderTotalPrice;
    private OrderStatusEnum orderStatus;

    private OrderCancelResponseDto(
            UUID orderId,
            UUID productId,
            Integer orderQuantity,
            BigDecimal orderTotalPrice,
            OrderStatusEnum orderStatus
    ) {
        this.orderId = orderId;
        this.productId = productId;
        this.orderQuantity = orderQuantity;
        this.orderTotalPrice = orderTotalPrice;
        this.orderStatus = orderStatus;
    }

    public static OrderCancelResponseDto fromOrderCancel(
            OrderEntity orderEntity
    ){
        return new OrderCancelResponseDto(
                orderEntity.getOrderId(),
                orderEntity.getProductId(),
                orderEntity.getOrderQuantity(),
                orderEntity.getOrderTotalPrice(),
                orderEntity.getOrderStatus()
        );
    }
}
