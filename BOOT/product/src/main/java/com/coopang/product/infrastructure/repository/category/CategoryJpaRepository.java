package com.coopang.product.infrastructure.repository.category;

import com.coopang.product.domain.entity.category.CategoryEntity;
import com.coopang.product.domain.repository.category.CategoryRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, UUID>, CategoryRepository {

}
