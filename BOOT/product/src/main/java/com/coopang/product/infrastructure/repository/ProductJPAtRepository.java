package com.coopang.product.infrastructure.repository;

import com.coopang.product.domain.entitiy.ProductEntity;
import com.coopang.product.domain.repository.ProductRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductJPAtRepository extends JpaRepository<ProductEntity, UUID>, ProductRepository {

}
