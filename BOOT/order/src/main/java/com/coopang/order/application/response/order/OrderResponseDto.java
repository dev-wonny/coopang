package com.coopang.order.application.response.order;

import com.coopang.apidata.application.address.Address;
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
    private UUID nearHubId;
    private UUID productHubId;
    private Address address;
    private Integer orderQuantity;
    private BigDecimal orderSinglePrice;
    private BigDecimal orderTotalPrice;
    private String orderStatus;
    private boolean isDeleted;

    private OrderResponseDto(
            UUID orderId,
            UUID userId,
            UUID productId,
            UUID nearHubId,
            UUID productHubId,
            Address address,
            Integer orderQuantity,
            BigDecimal orderSinglePrice,
            BigDecimal orderTotalPrice,
            String orderStatus,
            boolean isDeleted
    ){
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.nearHubId = nearHubId;
        this.productHubId = productHubId;
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
                orderEntity.getNearHubId(),
                orderEntity.getProductHubId(),
                new Address(orderEntity.getAddressEntity().getZipCode(),orderEntity.getAddressEntity().getAddress1(),orderEntity.getAddressEntity().getAddress2()),
                orderEntity.getOrderQuantity(),
                orderEntity.getOrderSinglePrice(),
                orderEntity.getOrderTotalPrice(),
                orderEntity.getOrderStatus().toString(),
                orderEntity.isDeleted()
                );
    }
}
