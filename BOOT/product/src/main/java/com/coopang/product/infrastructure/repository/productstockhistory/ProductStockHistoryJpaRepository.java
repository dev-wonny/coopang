package com.coopang.product.infrastructure.repository.productstockhistory;

import com.coopang.product.domain.entity.productstockhistory.ProductStockHistoryEntity;
import com.coopang.product.domain.repository.productstockhistory.ProductStockHistoryRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockHistoryJpaRepository extends JpaRepository<ProductStockHistoryEntity, UUID>, ProductStockHistoryRepositoryCustom,
    ProductStockHistoryRepository {

}
