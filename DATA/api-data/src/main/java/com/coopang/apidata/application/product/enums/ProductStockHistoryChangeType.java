package com.coopang.apidata.application.product.enums;

import lombok.Getter;

@Getter
public enum ProductStockHistoryChangeType {
    INCREASE("상품 재고 증가"),
    DECREASE("상품 재고 감소");

    private final String description;

    ProductStockHistoryChangeType(String description) {
        this.description = description;
    }
}
