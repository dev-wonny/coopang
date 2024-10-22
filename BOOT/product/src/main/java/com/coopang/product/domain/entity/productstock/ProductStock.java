package com.coopang.product.domain.entity.productstock;

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
    private Integer value;

    //TODO : 에러 메시지는 api-data에 추후 추가후 수정예정
    public ProductStock(int value) {

        if(value < 0) {
            throw new IllegalArgumentException("추가할 수량은 음수가 될 수 없습니다.");
        }

        this.value = value == 0 ? 100 : value;

    }

    public void addStock(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("추가할 수량은 음수가 될 수 없습니다.");
        }
        this.value += amount;
    }

    public void reduceStock(int amount) {
        if (this.value - amount < 0) {
            throw new IllegalArgumentException("재고 수량이 부족합니다.");
        }
        this.value -= amount;
    }
}
