package com.coopang.product.application.request.Productstockhistory;

import com.coopang.product.domain.entity.productstockhistory.ProductStockHistoryChangeType;
import lombok.Data;

@Data
public class ProductStockHistoryDto {
    private int productStockHistoryChangeQuantity;
    private int productStockHistoryPreviousQuantity;
    private int productStockHistoryCurrentQuantity;
    private String productStockHistoryAdditionalInfo;
    private ProductStockHistoryChangeType productStockHistoryChangeType;
}
