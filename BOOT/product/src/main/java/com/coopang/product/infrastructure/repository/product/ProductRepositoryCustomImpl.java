package com.coopang.product.infrastructure.repository.product;

import static com.coopang.product.domain.entity.category.QCategoryEntity.categoryEntity;
import static com.coopang.product.domain.entity.product.QProductEntity.productEntity;
import static com.coopang.product.domain.entity.productstock.QProductStockEntity.productStockEntity;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.product.application.request.product.ProductSearchConditionDto;
import com.coopang.product.domain.entity.product.ProductEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class ProductRepositoryCustomImpl extends Querydsl4RepositorySupport implements
    ProductRepositoryCustom {

    public ProductRepositoryCustomImpl() {
        super(ProductEntity.class);
    }

    @Override
    public Optional<ProductEntity> getValidOneByProductIdWithCategory(UUID productId) {

        return Optional.ofNullable(selectFrom(productEntity)
            .leftJoin(productEntity.categoryEntity, categoryEntity)
            .fetchJoin()
            .where(
                productEntity.productId.eq(productId),
                productEntity.isDeleted.eq(false),
                productEntity.isHidden.eq(false),
                productEntity.isSale.eq(true)
            ).fetchOne());
    }

    @Override
    public Optional<ProductEntity> getOneByProductIdWithCategory(UUID productId) {

        return Optional.ofNullable(selectFrom(productEntity)
            .leftJoin(productEntity.categoryEntity, categoryEntity)
            .fetchJoin()
            .where(
                productEntity.productId.eq(productId)
            ).fetchOne());
    }


    @Override
    public Page<ProductEntity> search(ProductSearchConditionDto productSearchCondition,
                                      Pageable pageable) {

        return applyPagination(pageable, contentQuery ->
                contentQuery.selectFrom(productEntity)
                    .leftJoin(productEntity.productStockEntity, productStockEntity)
                    .leftJoin(productEntity.categoryEntity, categoryEntity)
                    .fetchJoin()
                    .where(
                        productNameContains(productSearchCondition.getProductName()),
                        companyIdEq(productSearchCondition.getCompanyId()),
                        productIdEq(productSearchCondition.getProductId()),
                        betweenStartDateAndEndDate(productSearchCondition.getStartDate(), productSearchCondition.getEndDate()),
                        isProductPriceGreaterThan(productSearchCondition.getMinProductPrice()),
                        isProductPricelessThan(productSearchCondition.getMaxProductPrice()),
                        isAbleToSearchProductIsDeleted(productSearchCondition.getIsAbleToWatchDeleted()),
                        productStockEntity.productStock.currentStockQuantity.ne(0)
                    ),
            countQuery -> countQuery.select(
                    productEntity.productId
                ).from(productEntity)
                .where(
                    productNameContains(productSearchCondition.getProductName()),
                    companyIdEq(productSearchCondition.getCompanyId()),
                    productIdEq(productSearchCondition.getProductId()),
                    betweenStartDateAndEndDate(productSearchCondition.getStartDate(), productSearchCondition.getEndDate()),
                    isProductPriceGreaterThan(productSearchCondition.getMinProductPrice()),
                    isProductPricelessThan(productSearchCondition.getMaxProductPrice()),
                    isAbleToSearchProductIsDeleted(productSearchCondition.getIsAbleToWatchDeleted()),
                    productStockEntity.productStock.currentStockQuantity.ne(0)
                )
        );
    }

    private Predicate isAbleToSearchProductIsDeleted(
        boolean isAbleToSearchProductIsDeleted) {

        BooleanBuilder builder = new BooleanBuilder();

        // isDeleted 조건
        if (!isAbleToSearchProductIsDeleted) {

            builder.and(productEntity.isDeleted.eq(false));
            builder.and(productEntity.isSale.eq(true));
            builder.and(productEntity.isHidden.eq(false));
        }

        return builder;
    }

    private Predicate productNameContains(String productName) {
        return StringUtils.hasText(productName) ? productEntity.productName.contains(productName) : null;
    }

    private Predicate productIdEq(UUID productId) {
        return productId != null ? productEntity.productId.eq(productId) : null;
    }

    private Predicate companyIdEq(UUID companyId) {
        return companyId != null ? productEntity.companyId.eq(companyId) : null;
    }

    private Predicate betweenStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate) {

        if (startDate == null && endDate == null) {
            return null;
        }

        if (startDate == null) {
            return productEntity.createdAt.loe(endDate);
        }

        if (endDate == null) {

            return productEntity.createdAt.goe(startDate);
        }

        return productEntity.createdAt.between(startDate, endDate);
    }

    private Predicate isProductPriceGreaterThan(double minProductPrice) {
        return productEntity.productPrice.goe(minProductPrice);
    }

    private Predicate isProductPricelessThan(double maxProductPrice) {
        maxProductPrice = maxProductPrice == 0.0 ? Double.MAX_VALUE : maxProductPrice;

        return productEntity.productPrice.loe(maxProductPrice);
    }
}
