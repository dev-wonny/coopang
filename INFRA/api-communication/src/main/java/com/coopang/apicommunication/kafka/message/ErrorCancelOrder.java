package com.coopang.apicommunication.kafka.message;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ErrorCancelOrder {
    private UUID orderId;
    private String errorMessage;
}
