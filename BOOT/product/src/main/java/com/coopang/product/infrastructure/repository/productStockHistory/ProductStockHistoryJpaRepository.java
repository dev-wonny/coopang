package com.coopang.product.infrastructure.repository.productStockHistory;

import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryEntity;
import com.coopang.product.domain.repository.productStockHistory.ProductStockHistoryRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockHistoryJpaRepository extends JpaRepository<ProductStockHistoryEntity, UUID>, ProductStockHistoryRepositoryCustom,
    ProductStockHistoryRepository {

}
