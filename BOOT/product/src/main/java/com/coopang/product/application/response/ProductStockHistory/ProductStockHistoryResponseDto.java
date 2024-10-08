package com.coopang.product.application.response.ProductStockHistory;

import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryChangeType;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryEntity;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ProductStockHistoryResponseDto(
    UUID productStockHistoryId,
    UUID productStockId,
    UUID orderId,
    int productStockHistoryChangeQuantity,
    int productStockHistoryPreviousQuantity,
    int productStockHistoryCurrentQuantity,
    String productStockHistoryAdditionalInfo,
    ProductStockHistoryChangeType productStockHistoryChangeType
) {

    public static ProductStockHistoryResponseDto of(ProductStockHistoryEntity entity){
        return ProductStockHistoryResponseDto.builder()
            .productStockHistoryId(entity.getProductStockHistoryId())
            .orderId(entity.getOrderId())
            .productStockHistoryChangeQuantity(entity.getProductStockHistoryChangeQuantity())
            .productStockHistoryPreviousQuantity(entity.getProductStockHistoryPreviousQuantity())
            .productStockHistoryCurrentQuantity(entity.getProductStockHistoryCurrentQuantity())
            .productStockHistoryAdditionalInfo(entity.getProductStockHistoryAdditionalInfo())
            .productStockHistoryChangeType(entity.getProductStockHistoryChangeType())
            .build();
    }
}
