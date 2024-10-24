package com.coopang.product.domain.repository.productstockhistory;

import com.coopang.product.domain.entity.productstockhistory.ProductStockHistoryEntity;
import com.coopang.product.presentation.request.productstockhistory.ProductStockHistorySearchConditionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductStockHistoryRepository {
    Page<ProductStockHistoryEntity> getProductStockHistoryByProductId(UUID productId, Pageable pageable);

    Page<ProductStockHistoryEntity> searchProductStockHistoryByProductId(ProductStockHistorySearchConditionDto condition, UUID productId, Pageable pageable);

    Optional<ProductStockHistoryEntity> findByProductStockHistoryIdAndProductStockEntity_ProductStockId(UUID productStockHistoryId, UUID productStockId);
}