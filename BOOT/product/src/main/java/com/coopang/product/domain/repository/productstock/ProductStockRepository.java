package com.coopang.product.domain.repository.productstock;

import com.coopang.product.domain.entity.productstock.ProductStockEntity;
import java.util.Optional;
import java.util.UUID;

public interface ProductStockRepository {

    Optional<ProductStockEntity> findByProductEntity_ProductId(UUID productId);

    ProductStockEntity findAndLockProductStock(UUID productId);
}
