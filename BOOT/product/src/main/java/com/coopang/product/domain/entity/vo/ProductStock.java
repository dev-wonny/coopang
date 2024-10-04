package com.coopang.product.domain.entity.vo;

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
    private Integer value;

    //TODO : 에러 메시지는 api-data에 추후 추가후 수정예정
    public ProductStock(int value) {

        if(value < 0) {
            throw new IllegalArgumentException("추가할 수량은 음수가 될 수 없습니다.");
        }

        this.value = value == 0 ? 100 : value;

    }

}
