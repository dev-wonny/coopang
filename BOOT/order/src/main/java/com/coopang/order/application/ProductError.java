package com.coopang.order.application;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductError {
    private UUID orderId;
    private String errorMessage;
}
