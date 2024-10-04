package com.coopang.product.infrastructure.entity.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.concurrent.atomic.AtomicInteger;
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
    private AtomicInteger value;

    //TODO : 에러 메시지는 api-data에 추후 추가후 수정예정
    public ProductStock(int value) {

        if(value < 0) {
            throw new IllegalArgumentException("추가할 수량은 음수가 될 수 없습니다.");
        }

        this.value = value == 0 ? new AtomicInteger(100) : new AtomicInteger(value);

    }

    // 재고 추가 메서드
    public void add(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("입고할 수량은 음수가 될 수 없습니다.");
        }
        this.value.addAndGet(amount);
    }

    // 재고 감소 메서드
    public void remove(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("출고할 수량은 음수가 될 수 없습니다.");
        }
        if (this.value.get() < amount) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.value.addAndGet(-amount);
    }

    public int getValue(){
        return this.value.get();
    }
}
