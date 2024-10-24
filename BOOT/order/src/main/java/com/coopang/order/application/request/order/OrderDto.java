package com.coopang.order.application.request.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class OrderDto {
    /*
    제품의 대한 정보
     */
    private UUID productId;
    private UUID productHubId;
    private Integer orderQuantity;
    private BigDecimal orderSinglePrice;
    private BigDecimal orderTotalPrice;
    /*
    배송지에 대한 정보
     */
    private String zipCode;
    private String address1;
    private String address2;
    private UUID nearHubId;

    @Builder
    private OrderDto(
            UUID productId
            , UUID productHubId
            , Integer orderQuantity
            , BigDecimal orderSinglePrice
            , BigDecimal orderTotalPrice
            , String zipCode
            , String address1
            , String address2
            , UUID nearHubId
    ) {
        this.productId = productId;
        this.productHubId = productHubId;
        this.orderQuantity = orderQuantity;
        this.orderSinglePrice = orderSinglePrice;
        this.orderTotalPrice = orderTotalPrice;
        this.zipCode = zipCode;
        this.address1 = address1;
        this.address2 = address2;
        this.nearHubId = nearHubId;
    }

    public static OrderDto of(
            UUID productId
            , UUID productHubId
            , Integer orderQuantity
            , BigDecimal orderSinglePrice
            , BigDecimal orderTotalPrice
            , String zipCode
            , String address1
            , String address2
            , UUID nearHubId
    ) {
        return OrderDto.builder()
                .productId(productId)
                .productHubId(productHubId)
                .orderQuantity(orderQuantity)
                .orderSinglePrice(orderSinglePrice)
                .orderTotalPrice(orderTotalPrice)
                .zipCode(zipCode)
                .address1(address1)
                .address2(address2)
                .nearHubId(nearHubId)
                .build();
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "productId=" + productId +
                ", productHubId=" + productHubId +
                ", orderQuantity=" + orderQuantity +
                ", orderSinglePrice=" + orderSinglePrice +
                ", orderTotalPrice=" + orderTotalPrice +
                ", zipCode='" + zipCode + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", nearHubId=" + nearHubId +
                '}';
    }
}

