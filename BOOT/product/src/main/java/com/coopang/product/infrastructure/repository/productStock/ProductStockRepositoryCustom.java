package com.coopang.product.infrastructure.repository.productStock;

import com.coopang.product.domain.entity.productStock.ProductStockEntity;
import java.util.UUID;

public interface ProductStockRepositoryCustom {
    ProductStockEntity findAndLockProductStock(UUID productId);
}
