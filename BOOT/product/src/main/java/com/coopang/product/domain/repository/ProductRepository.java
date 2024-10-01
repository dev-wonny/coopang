package com.coopang.product.domain.repository;

import com.coopang.product.domain.entitiy.ProductEntity;
import java.util.Optional;

public interface ProductRepository {

    Optional<ProductEntity> findById(String id);

    Optional<ProductEntity> save(ProductEntity productEntity);
}
