package com.coopang.apicommunication.kafka.message;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CompletePayment {
    private UUID orderId;
    private UUID companyId;
    private String completeMessage;
}
