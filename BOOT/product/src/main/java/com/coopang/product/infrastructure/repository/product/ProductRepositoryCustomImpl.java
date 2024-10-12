package com.coopang.product.infrastructure.repository.product;

import static com.coopang.product.domain.entity.product.QProductEntity.productEntity;
import static com.coopang.product.domain.entity.category.QCategoryEntity.categoryEntity;
import static com.coopang.product.domain.entity.productStock.QProductStockEntity.productStockEntity;
import static com.coopang.product.domain.entity.productStockHistory.QProductStockHistoryEntity.productStockHistoryEntity;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.entity.productStock.ProductStockEntity;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryChangeType;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryEntity;
import com.coopang.product.presentation.request.product.ProductSearchCondition;
import com.coopang.product.presentation.request.productStockHistory.ProductStockHistorySearchCondition;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.LockModeType;
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
                .join(productEntity).fetchJoin()
                .on(productStockHistoryEntity.productStockEntity.productEntity.productId.eq(productEntity.productId))
                .where(productEntity.productId.eq(productId))
            ,countQuery -> countQuery.selectFrom(productStockHistoryEntity)
                .join(productStockHistoryEntity.productStockEntity)
                .join(productEntity).fetchJoin()
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
                    .join(productEntity).fetchJoin()
                    .on(productStockHistoryEntity.productStockEntity.productEntity.productId.eq(productEntity.productId))
                    .where(productEntity.productId.eq(productId),
                        isProductStockHistoryType(condition.changeType()),
                        betweenStartDateAndEndDateByProductStockHistory(condition.startDate(),condition.endDate()))
            ,countQuery -> countQuery.selectFrom(productStockHistoryEntity)
                .join(productStockHistoryEntity.productStockEntity)
                .join(productEntity).fetchJoin()
                .on(productStockHistoryEntity.productStockEntity.productEntity.productId.eq(productEntity.productId))
                .where(productEntity.productId.eq(productId),
                    isProductStockHistoryType(condition.changeType()),
                    betweenStartDateAndEndDateByProductStockHistory(condition.startDate(),condition.endDate()))
        );
    }

    @Override
    public ProductStockEntity findAndLockProductStock(UUID productId) {

        return selectFrom(productStockEntity)
            .join(productEntity)
            .on(productEntity.productStockEntity.productStockId.eq(productStockEntity.productStockId))
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .where(productStockEntity.isDeleted.eq(false),
                productEntity.productId.eq(productId))
            .fetchOne();
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
                    productNameContains(productSearchCondition.getProductName()),
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
                    productNameContains(productSearchCondition.getProductName()),
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

    private Predicate productNameContains(String productName) {
        return StringUtils.hasText(productName) ? productEntity.productName.contains(productName) : null;
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