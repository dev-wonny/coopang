package com.coopang.product.application.request;

import com.coopang.product.domain.entity.ProductStockHistoryChangeType;
import lombok.Data;

@Data
public class ProductStockHistoryDto {
    private int productStockHistoryChangeQuantity;
    private int productStockHistoryPreviousQuantity;
    private int productStockHistoryCurrentQuantity;
    private String productStockHistoryAdditionalInfo;
    private ProductStockHistoryChangeType productStockHistoryChangeType;
}
