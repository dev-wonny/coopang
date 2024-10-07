package com.coopang.order.application;

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
