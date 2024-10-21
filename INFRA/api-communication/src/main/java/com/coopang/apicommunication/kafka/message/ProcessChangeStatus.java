package com.coopang.apicommunication.kafka.message;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProcessChangeStatus {
    private UUID orderId;
    private String deliveryStatus;
}
