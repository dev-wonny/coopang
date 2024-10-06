package com.coopang.product.presentation.request;

import com.coopang.product.domain.entity.ProductStockHistoryChangeType;
import java.time.LocalDateTime;

public record ProductStockHistorySearchCondition(
    ProductStockHistoryChangeType changeType,
    LocalDateTime startDate,
    LocalDateTime endDate

) {

}
