package com.coopang.order.application.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class OrderDto {

    private UUID productId;
    private String zipCode;
    private String address1;
    private String address2;
    private Integer orderQuantity;
    private BigDecimal orderSinglePrice;
    private BigDecimal orderTotalPrice;
}
