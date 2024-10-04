package com.coopang.product.infrastructure.repository;

import com.coopang.product.infrastructure.entity.ProductEntity;
import com.coopang.product.presentation.request.ProductSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<ProductEntity> search(ProductSearchCondition productSearchCondition, Pageable pageable);
}
