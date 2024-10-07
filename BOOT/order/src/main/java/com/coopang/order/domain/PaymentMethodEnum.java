package com.coopang.order.domain;

import lombok.Getter;

@Getter
public enum PaymentMethodEnum {
    CARD("결제 방법 : 카드");

    private final String description;

    PaymentMethodEnum(String description) {
        this.description = description;
    }
}
