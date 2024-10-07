package com.coopang.product.presentation.request.productStockHistory;

import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryChangeType;
import java.time.LocalDateTime;

public record ProductStockHistorySearchCondition(
    ProductStockHistoryChangeType changeType,
    LocalDateTime startDate,
    LocalDateTime endDate

) {

}
