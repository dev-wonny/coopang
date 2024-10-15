package com.coopang.product.infrastructure.repository.productstock;

import com.coopang.product.domain.entity.productstock.ProductStockEntity;
import java.util.UUID;

public interface ProductStockRepositoryCustom {
    ProductStockEntity findAndLockProductStock(UUID productId);
}
