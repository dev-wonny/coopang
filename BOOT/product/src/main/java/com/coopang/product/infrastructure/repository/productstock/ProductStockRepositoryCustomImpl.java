package com.coopang.product.infrastructure.repository.productstock;

import static com.coopang.product.domain.entity.product.QProductEntity.productEntity;
import static com.coopang.product.domain.entity.productstock.QProductStockEntity.productStockEntity;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.product.domain.entity.productstock.ProductStockEntity;
import jakarta.persistence.LockModeType;
import java.util.UUID;

public class ProductStockRepositoryCustomImpl extends Querydsl4RepositorySupport implements ProductStockRepositoryCustom{

    public ProductStockRepositoryCustomImpl() {
        super(ProductStockEntity.class);
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
}
