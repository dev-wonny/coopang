package com.coopang.product.presentation.request.productstockhistory;

import com.coopang.product.domain.entity.productstockhistory.ProductStockHistoryChangeType;
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
