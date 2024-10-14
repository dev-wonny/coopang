package com.coopang.order.apicommnication;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CancelPayment {
    private UUID orderId;
}
