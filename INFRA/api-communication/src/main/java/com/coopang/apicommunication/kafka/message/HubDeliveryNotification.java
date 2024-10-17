package com.coopang.apicommunication.kafka.message;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class HubDeliveryNotification {
    private UUID deliveryShipperId;
    private String message;
}
