package com.coopang.apicommunication.kafka.message;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CompeleteProduct {
    private UUID orderId;
    private UUID companyId;
}
