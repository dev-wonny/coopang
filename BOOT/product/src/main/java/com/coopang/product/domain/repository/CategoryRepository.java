package com.coopang.product.domain.repository;

import com.coopang.product.domain.entity.CategoryEntity;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    Optional<CategoryEntity> findById(UUID categoryId);

}