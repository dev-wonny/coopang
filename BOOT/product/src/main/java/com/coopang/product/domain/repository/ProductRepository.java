package com.coopang.product.domain.repository;

import com.coopang.product.domain.entitiy.ProductEntity;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Optional<ProductEntity> findById(UUID productId);

    ProductEntity save(ProductEntity productEntity);
}
