package com.coopang.product.infrastructure.repository;

import com.coopang.product.application.response.ProductResponseDto;
import com.coopang.product.presentation.request.ProductSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<ProductResponseDto> search(ProductSearchCondition productSearchCondition, Pageable pageable);
}
