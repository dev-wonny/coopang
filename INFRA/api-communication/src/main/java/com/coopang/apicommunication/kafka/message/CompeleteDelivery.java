package com.coopang.apicommunication.kafka.message;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CompeleteDelivery {
    private UUID orderId;
    private UUID hubId;
    private UUID nearHubId;
    private UUID deliveryId;
    private String compeleteMessage;
}
