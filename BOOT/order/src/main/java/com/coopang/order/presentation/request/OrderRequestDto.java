package com.coopang.order.presentation.request;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class OrderRequestDto {

    private UUID productId;
    private String zipCode;
    private String address1;
    private String address2;
    private Integer orderQuantity;
    private BigDecimal orderSinglePrice;
    private BigDecimal orderTotalPrice;
}
