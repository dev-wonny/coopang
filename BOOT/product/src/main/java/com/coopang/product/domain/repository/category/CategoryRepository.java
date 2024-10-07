package com.coopang.product.domain.repository.category;

import com.coopang.product.domain.entity.category.CategoryEntity;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    Optional<CategoryEntity> findById(UUID categoryId);

}
