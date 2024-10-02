package com.coopang.product.domain.entitiy.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductStock {

    @Column(nullable = false,name = "product_stock_current_quantity")
    private int value;

    public ProductStock(int value) {
        if(value < 0) {
            throw new IllegalArgumentException("재고의 수량은 음수가 될 수 없습니다.");
        }

        this.value = value;
    }

}
