package com.coopang.product.domain.repository.category;

import com.coopang.product.domain.entity.category.CategoryEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {

    Optional<CategoryEntity> findByCategoryIdAndIsDeletedFalse(UUID categoryId);

    List<CategoryEntity> findAllByIsDeletedFalse();
}
