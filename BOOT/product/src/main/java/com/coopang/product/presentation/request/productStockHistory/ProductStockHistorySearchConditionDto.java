package com.coopang.product.presentation.request.productStockHistory;

import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryChangeType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductStockHistorySearchConditionDto{
    private ProductStockHistoryChangeType changeType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
