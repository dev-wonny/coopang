package com.coopang.apicommunication.kafka.message;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProcessDelivery {
    private UUID orderId;
    private UUID userId;
    private UUID nearHubId;
    private UUID productHubId;
    private String zipCode;
    private String address1;
    private String address2;

}
