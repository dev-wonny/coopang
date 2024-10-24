package com.coopang.order.application.response.order;

import com.coopang.apidata.application.address.Address;
import com.coopang.order.domain.entity.order.OrderEntity;
import lombok.Builder;
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

    @Builder
    private OrderResponseDto(
            UUID orderId
            , UUID userId
            , UUID productId
            , UUID nearHubId
            , UUID productHubId
            , Address address
            , Integer orderQuantity
            , BigDecimal orderSinglePrice
            , BigDecimal orderTotalPrice
            , String orderStatus
            , boolean isDeleted
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

    public static OrderResponseDto fromOrder(OrderEntity order) {
        return OrderResponseDto
                .builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .productId(order.getProductId())
                .nearHubId(order.getNearHubId())
                .productHubId(order.getProductHubId())
                .address(new Address(
                        order.getAddressEntity().getZipCode()
                        , order.getAddressEntity().getAddress1()
                        , order.getAddressEntity().getAddress2()
                ))
                .orderQuantity(order.getOrderQuantity())
                .orderSinglePrice(order.getOrderSinglePrice())
                .orderTotalPrice(order.getOrderTotalPrice())
                .orderStatus(order.getOrderStatus().toString())
                .isDeleted(order.isDeleted())
                .build();
    }
}
