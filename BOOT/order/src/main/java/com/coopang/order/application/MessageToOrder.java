package com.coopang.order.application;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class MessageToOrder {
    private UUID orderId;
    private String message;
}
