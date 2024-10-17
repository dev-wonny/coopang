package com.coopang.product.infrastructure.repository.productStock;

import com.coopang.product.domain.entity.productStock.ProductStockEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockJpaRepository extends JpaRepository<ProductStockEntity, UUID> {
    
}
