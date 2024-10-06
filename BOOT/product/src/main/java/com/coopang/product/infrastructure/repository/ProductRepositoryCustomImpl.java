package com.coopang.product.infrastructure.repository;

import static com.coopang.product.domain.entity.QProductEntity.productEntity;
import static com.coopang.product.domain.entity.QCategoryEntity.categoryEntity;
import static com.coopang.product.domain.entity.QProductStockEntity.productStockEntity;
import static com.coopang.product.domain.entity.QProductStockHistoryEntity.productStockHistoryEntity;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.product.domain.entity.ProductEntity;
import com.coopang.product.domain.entity.ProductStockHistoryChangeType;
import com.coopang.product.domain.entity.ProductStockHistoryEntity;
import com.coopang.product.presentation.request.ProductSearchCondition;
import com.coopang.product.presentation.request.ProductStockHistorySearchCondition;
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
    public Optional<ProductEntity> getOneByProductId(UUID productId) {

        return Optional.ofNullable(selectFrom(productEntity)
            .leftJoin(productEntity.productStockEntity,productStockEntity)
            .leftJoin(productEntity.categoryEntity,categoryEntity)
            .fetchJoin()
            .where(
                productEntity.productId.eq(productId),
                productEntity.isDeleted.eq(false),
                productEntity.isHidden.eq(false),
                productEntity.isSale.eq(true)
            ).fetchOne());
    }

    @Override
    public Page<ProductStockHistoryEntity> getProductStockHistoryByProductId(UUID productId, Pageable pageable) {

        return applyPagination(pageable,contentQuery ->

             contentQuery.selectFrom(productStockHistoryEntity)
                .join(productStockHistoryEntity.productStockEntity)
                .join(productEntity)
                .on(productStockHistoryEntity.productStockEntity.productEntity.productId.eq(productEntity.productId))
                .where(productEntity.productId.eq(productId))
            ,countQuery -> countQuery.selectFrom(productStockHistoryEntity)
                .join(productStockHistoryEntity.productStockEntity)
                .join(productEntity)
                .on(productStockHistoryEntity.productStockEntity.productEntity.productId.eq(productEntity.productId))
                .where(productEntity.productId.eq(productId))
        );
    }

    @Override
    public Page<ProductStockHistoryEntity> searchProductStockHistoryByProductId(
        ProductStockHistorySearchCondition condition, UUID productId, Pageable pageable) {


        return applyPagination(pageable,contentQuery ->
                contentQuery.selectFrom(productStockHistoryEntity)
                    .join(productStockHistoryEntity.productStockEntity)
                    .join(productEntity)
                    .on(productStockHistoryEntity.productStockEntity.productEntity.productId.eq(productEntity.productId))
                    .where(productEntity.productId.eq(productId),
                        isProductStockHistoryType(condition.changeType()),
                        betweenStartDateAndEndDateByProductStockHistory(condition.startDate(),condition.endDate()))
            ,countQuery -> countQuery.selectFrom(productStockHistoryEntity)
                .join(productStockHistoryEntity.productStockEntity)
                .join(productEntity)
                .on(productStockHistoryEntity.productStockEntity.productEntity.productId.eq(productEntity.productId))
                .where(productEntity.productId.eq(productId),
                    isProductStockHistoryType(condition.changeType()),
                    betweenStartDateAndEndDateByProductStockHistory(condition.startDate(),condition.endDate()))
        );
    }

    private Predicate isProductStockHistoryType(ProductStockHistoryChangeType changeType) {
        return changeType != null ? productStockHistoryEntity.productStockHistoryChangeType.eq(changeType) : null ;
    }

    private Predicate betweenStartDateAndEndDateByProductStockHistory(LocalDateTime startDate, LocalDateTime endDate) {

        if(startDate==null && endDate==null) {
            return null;
        }

        if (startDate == null) {
            return productStockHistoryEntity.createdAt.before(endDate);
        }

        if(endDate==null) {

            return productStockHistoryEntity.createdAt.after(startDate);
        }

        return productStockHistoryEntity.createdAt.between(startDate, endDate);
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
                    productNameEq(productSearchCondition.getProductName()),
                    companyIdEq(productSearchCondition.getCompanyId()),
                    betweenStartDateAndEndDate(productSearchCondition.getStartDate(), productSearchCondition.getEndDate()),
                    isProductPriceGreaterThan(productSearchCondition.getMinProductPrice()),
                    isProductPricelessThan(productSearchCondition.getMaxProductPrice()),
                    productEntity.isDeleted.eq(false),
                    productEntity.isHidden.eq(false),
                    productEntity.isSale.eq(true),
                    productStockEntity.productStock.value.ne(0)
                ),
            countQuery -> countQuery.select(
                    productEntity.productId
                ).from(productEntity)
                .where(
                    productNameEq(productSearchCondition.getProductName()),
                    companyIdEq(productSearchCondition.getCompanyId()),
                    betweenStartDateAndEndDate(productSearchCondition.getStartDate(), productSearchCondition.getEndDate()),
                    isProductPriceGreaterThan(productSearchCondition.getMinProductPrice()),
                    isProductPricelessThan(productSearchCondition.getMaxProductPrice()),
                    productEntity.isDeleted.eq(false),
                    productEntity.isHidden.eq(false),
                    productEntity.isSale.eq(true),
                    productStockEntity.productStock.value.ne(0)
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

        if (startDate == null) {
            return productEntity.createdAt.before(endDate);
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
