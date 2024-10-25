package com.coopang.apidata.application.payment.enums;

import lombok.Getter;

@Getter
public enum PaymentMethodEnum {
    CARD("결제 방법 : 카드");

    private final String description;

    PaymentMethodEnum(String description) {
        this.description = description;
    }
}
