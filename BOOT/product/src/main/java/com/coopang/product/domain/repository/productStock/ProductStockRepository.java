package com.coopang.product.domain.repository.productStock;

import com.coopang.product.domain.entity.productStock.ProductStockEntity;
import java.util.Optional;
import java.util.UUID;

public interface ProductStockRepository {

    Optional<ProductStockEntity> findByProductEntity_ProductId(UUID productId);

    ProductStockEntity findAndLockProductStock(UUID productId);
}
