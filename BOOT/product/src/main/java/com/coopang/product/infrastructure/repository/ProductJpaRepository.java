package com.coopang.product.infrastructure.repository;

import com.coopang.product.domain.entity.ProductEntity;
import com.coopang.product.domain.repository.ProductRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID>, ProductRepository,
    ProductRepositoryCustom {

}
