package com.coopang.product.infrastructure.repository.product;

import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.presentation.request.product.ProductSearchCondition;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Optional<ProductEntity> getOneByProductIdWithCategory(UUID productId);

    Page<ProductEntity> search(ProductSearchCondition productSearchCondition, Pageable pageable);
}
