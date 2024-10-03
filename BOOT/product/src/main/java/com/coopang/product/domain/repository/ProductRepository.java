package com.coopang.product.domain.repository;

import com.coopang.product.domain.entitiy.ProductEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository {

    Optional<ProductEntity> findById(UUID productId);

    ProductEntity save(ProductEntity productEntity);

    Page<ProductEntity> findAll(Pageable pageable);

}
