package com.coopang.apidata.application.order.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    READY("생성했을때 → 재고체크, 결제에 전달"),
    PENDING("주문 생성 ,중간 단계 : 데이터만 삽입해서 안함"),
    SHIPPED("배송 등록 완료(배송 중) (주문 취소 불가능)"),
    DELIVERED("주문이 고객에게 전달된 상태"),
    CANCELED("주문이 취소된 상태");

    private final String description;

    OrderStatusEnum(String description) {
        this.description = description;
    }

    public static OrderStatusEnum getByDescription(String description) {
        try {
            return OrderStatusEnum.valueOf(description);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + description, e);
        }
    }
}
