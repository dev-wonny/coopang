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

    @Column(nullable = false, name = "product_stock_current_quantity")
    private Integer currentStockQuantity;

    //TODO : 에러 메시지는 api-data에 추후 추가후 수정예정
    public ProductStock(int currentStockQuantity) {

        if (currentStockQuantity < 0) {
            throw new IllegalArgumentException("추가할 수량은 음수가 될 수 없습니다.");
        }

        this.currentStockQuantity = currentStockQuantity == 0 ? 100 : currentStockQuantity;

    }

    public void addStock(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        this.currentStockQuantity += amount;
    }

    public void reduceStock(int amount) {
        if (this.currentStockQuantity - amount < 0) {
            throw new IllegalArgumentException();
        }
        this.currentStockQuantity -= amount;
    }
}
