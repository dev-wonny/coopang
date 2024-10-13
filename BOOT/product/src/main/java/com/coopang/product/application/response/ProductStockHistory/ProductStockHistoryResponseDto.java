package com.coopang.product.application.response.ProductStockHistory;

import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryChangeType;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryEntity;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ProductStockHistoryResponseDto
{
    private UUID productStockHistoryId;
    private UUID productStockId;
    private UUID orderId;
    private int productStockHistoryChangeQuantity;
    private int productStockHistoryPreviousQuantity;
    private int productStockHistoryCurrentQuantity;
    private String productStockHistoryAdditionalInfo;
    private ProductStockHistoryChangeType productStockHistoryChangeType;


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
