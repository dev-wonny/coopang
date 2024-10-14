package com.coopang.product.domain.repository.productStockHistory;

import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryEntity;
import com.coopang.product.presentation.request.productStockHistory.ProductStockHistorySearchConditionDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductStockHistoryRepository {

    Page<ProductStockHistoryEntity> getProductStockHistoryByProductId(
        UUID productId, Pageable pageable);

    Page<ProductStockHistoryEntity> searchProductStockHistoryByProductId(
        ProductStockHistorySearchConditionDto condition,UUID productId, Pageable pageable);
}
