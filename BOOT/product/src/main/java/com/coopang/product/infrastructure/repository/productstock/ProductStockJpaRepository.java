package com.coopang.product.infrastructure.repository.productstock;

import com.coopang.product.domain.entity.productstock.ProductStockEntity;
import com.coopang.product.domain.repository.productstock.ProductStockRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockJpaRepository extends JpaRepository<ProductStockEntity, UUID>,
    ProductStockRepository,ProductStockRepositoryCustom {
    
}
