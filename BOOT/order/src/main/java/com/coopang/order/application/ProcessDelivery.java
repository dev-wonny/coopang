package com.coopang.order.application;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProcessDelivery {
    private UUID orderId;
    private UUID userId;
    private UUID companyId;
    private String zipCode;
    private String address1;
    private String address2;
}
