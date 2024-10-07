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
public class OrderResponseDto {

    private UUID orderId;
    private UUID productId;
    private UUID userId;
    private Address address;
    private Integer orderQuantity;
    private BigDecimal orderSinglePrice;
    private BigDecimal orderTotalPrice;
    private OrderStatusEnum orderStatus;
    private boolean isDeleted;

    private OrderResponseDto(
            UUID orderId,
            UUID userId,
            UUID productId,
            Address address,
            Integer orderQuantity,
            BigDecimal orderSinglePrice,
            BigDecimal orderTotalPrice,
            OrderStatusEnum orderStatus,
            boolean isDeleted
    ){
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.address = address;
        this.orderQuantity = orderQuantity;
        this.orderSinglePrice = orderSinglePrice;
        this.orderTotalPrice = orderTotalPrice;
        this.orderStatus = orderStatus;
        this.isDeleted = isDeleted;
    }

    public static OrderResponseDto fromOrder(OrderEntity orderEntity) {
        return new OrderResponseDto(
                orderEntity.getOrderId(),
                orderEntity.getUserId(),
                orderEntity.getProductId(),
                new Address(orderEntity.getAddressEntity().getZipCode(),orderEntity.getAddressEntity().getAddress1(),orderEntity.getAddressEntity().getAddress2()),
                orderEntity.getOrderQuantity(),
                orderEntity.getOrderSinglePrice(),
                orderEntity.getOrderTotalPrice(),
                orderEntity.getOrderStatus(),
                orderEntity.isDeleted()
                );
    }
}
