package com.coopang.product.infrastructure.repository;

import com.coopang.product.domain.entity.CategoryEntity;
import com.coopang.product.domain.repository.CategoryRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, UUID>, CategoryRepository {

}
