package com.coopang.product.infrastructure.repository;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.product.application.response.ProductResponseDto;
import com.coopang.product.domain.entitiy.ProductEntity;
import com.coopang.product.presentation.request.ProductSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ProductRepositoryCustomImpl extends Querydsl4RepositorySupport implements ProductRepositoryCustom {

    public ProductRepositoryCustomImpl() {
        super(ProductEntity.class);
    }

    @Override
    public Page<ProductResponseDto> search(ProductSearchCondition productSearchCondition,
        Pageable pageable) {

        return null;
        //return applyPagination(pageable, contentQuery -> contentQuery.selectFrom(ProductEntity));
    }
}
