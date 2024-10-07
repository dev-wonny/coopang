package com.coopang.order.presentation.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PGRequestDto {
    private String paymentMethod;
    private BigDecimal orderTotalPrice;
}
