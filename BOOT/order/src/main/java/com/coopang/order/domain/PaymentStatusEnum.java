package com.coopang.order.domain;

import lombok.Getter;

@Getter
public enum PaymentStatusEnum {
    COMPLETED("결제 완료"),
    FAILED("결제 실패"),
    CANCELED("결제 취소");

    private final String description;

    PaymentStatusEnum(String description) {
        this.description = description;
    }
}
