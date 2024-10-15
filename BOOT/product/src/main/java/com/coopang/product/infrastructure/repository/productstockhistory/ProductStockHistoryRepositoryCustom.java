package com.coopang.product.infrastructure.repository.productstockhistory;

import com.coopang.product.domain.entity.productstockhistory.ProductStockHistoryEntity;
import com.coopang.product.presentation.request.productstockhistory.ProductStockHistorySearchConditionDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductStockHistoryRepositoryCustom {

    Page<ProductStockHistoryEntity> getProductStockHistoryByProductId(
        UUID productId, Pageable pageable);

    Page<ProductStockHistoryEntity> searchProductStockHistoryByProductId(
        ProductStockHistorySearchConditionDto condition,UUID productId, Pageable pageable);
}
