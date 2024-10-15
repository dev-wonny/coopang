package com.coopang.product.application.response.productStock;

import com.coopang.product.domain.entity.productstock.ProductStockEntity;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ProductStockResponseDto {
    private UUID productStockId;
    private int stock;

    public static ProductStockResponseDto of(ProductStockEntity productStock) {
        return ProductStockResponseDto.builder()
            .productStockId(productStock.getProductStockId())
            .stock(productStock.getProductStock().getValue())
            .build();
    }
}
