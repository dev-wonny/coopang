package com.coopang.product.infrastructure.repository.product;

import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.presentation.request.product.ProductSearchConditionDto;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Optional<ProductEntity> getOneByProductIdWithCategory(UUID productId);

    Optional<ProductEntity> getValidOneByProductIdWithCategory(UUID productId);

    Page<ProductEntity> search(ProductSearchConditionDto productSearchCondition, Pageable pageable);
}
