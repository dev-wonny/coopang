package com.coopang.product.infrastructure.repository;

import static com.coopang.product.domain.entitiy.QProductEntity.productEntity;
import static com.coopang.product.domain.entitiy.QCategoryEntity.categoryEntity;
import static com.coopang.product.domain.entitiy.QProductStockEntity.productStockEntity;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.product.domain.entitiy.ProductEntity;
import com.coopang.product.presentation.request.ProductSearchCondition;
import com.querydsl.core.types.Predicate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

public class ProductRepositoryCustomImpl extends Querydsl4RepositorySupport implements
    ProductRepositoryCustom {

    public ProductRepositoryCustomImpl() {
        super(ProductEntity.class);
    }

    @Override
    public Page<ProductEntity> search(ProductSearchCondition productSearchCondition,
        Pageable pageable) {

        return applyPagination(pageable, contentQuery ->
            contentQuery.selectFrom(productEntity)
                .leftJoin(productEntity.productStockEntity,productStockEntity)
                .leftJoin(productEntity.categoryEntity,categoryEntity)
                .fetchJoin()
                .where(
                    productNameEq(productSearchCondition.productName()),
                    companyIdEq(productSearchCondition.companyId()),
                    betweenStartDateAndEndDate(productSearchCondition.startDate(), productSearchCondition.endDate()),
                    isProductPriceGreaterThan(productSearchCondition.minProductPrice()),
                    isProductPricelessThan(productSearchCondition.maxProductPrice())
                ),
            countQuery -> countQuery.select(
                    productEntity.productId

                ).from(productEntity)
                .where(
                    productNameEq(productSearchCondition.productName()),
                    companyIdEq(productSearchCondition.companyId()),
                    betweenStartDateAndEndDate(productSearchCondition.startDate(), productSearchCondition.endDate()),
                    isProductPriceGreaterThan(productSearchCondition.minProductPrice()),
                    isProductPricelessThan(productSearchCondition.maxProductPrice())
                )
        );
    }

    private Predicate productNameEq(String productName) {
        return StringUtils.hasText(productName) ? productEntity.productName.eq(productName) : null;
    }

    private Predicate companyIdEq(UUID companyId) {
        return companyId != null ? productEntity.companyId.eq(companyId) : null;
    }

    private Predicate betweenStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate) {

        if(startDate==null && endDate==null) {
            return null;
        }

        if(endDate==null) {
            return productEntity.createdAt.after(startDate);

        }

        return productEntity.createdAt.between(startDate, endDate);
    }

    private Predicate isProductPriceGreaterThan(double minProductPrice) {
        return Optional.ofNullable(minProductPrice).map(price ->productEntity.productPrice.goe(price)).orElse(null);
    }

    private Predicate isProductPricelessThan(double maxProductPrice) {
        return Optional.ofNullable(maxProductPrice).map(price ->productEntity.productPrice.loe(price)).orElse(null);
    }
}
