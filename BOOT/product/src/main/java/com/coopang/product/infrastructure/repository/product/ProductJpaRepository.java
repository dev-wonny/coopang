package com.coopang.product.infrastructure.repository.product;

import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.repository.product.ProductRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID>, ProductRepository,
    ProductRepositoryCustom {

}
