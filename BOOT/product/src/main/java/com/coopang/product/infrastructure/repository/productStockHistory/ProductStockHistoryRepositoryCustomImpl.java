package com.coopang.product.infrastructure.repository.productStockHistory;

import static com.coopang.product.domain.entity.product.QProductEntity.productEntity;
import static com.coopang.product.domain.entity.productStockHistory.QProductStockHistoryEntity.productStockHistoryEntity;
import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryChangeType;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryEntity;
import com.coopang.product.presentation.request.productStockHistory.ProductStockHistorySearchConditionDto;
import com.querydsl.core.types.Predicate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ProductStockHistoryRepositoryCustomImpl extends Querydsl4RepositorySupport implements
    ProductStockHistoryRepositoryCustom {

    public ProductStockHistoryRepositoryCustomImpl() {
        super(ProductStockHistoryEntity.class);
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
        ProductStockHistorySearchConditionDto condition, UUID productId, Pageable pageable) {

        return applyPagination(pageable,contentQuery ->
                contentQuery.selectFrom(productStockHistoryEntity)
                    .join(productStockHistoryEntity.productStockEntity)
                    .join(productEntity).fetchJoin()
                    .on(productStockHistoryEntity.productStockEntity.productEntity.productId.eq(productEntity.productId))
                    .where(productEntity.productId.eq(productId),
                        isProductStockHistoryType(condition.getChangeType()),
                        betweenStartDateAndEndDateByProductStockHistory(condition.getStartDate(),condition.getEndDate()))
            ,countQuery -> countQuery.selectFrom(productStockHistoryEntity)
                .join(productStockHistoryEntity.productStockEntity)
                .join(productEntity).fetchJoin()
                .on(productStockHistoryEntity.productStockEntity.productEntity.productId.eq(productEntity.productId))
                .where(productEntity.productId.eq(productId),
                    isProductStockHistoryType(condition.getChangeType()),
                    betweenStartDateAndEndDateByProductStockHistory(condition.getStartDate(),condition.getEndDate()))
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
}
