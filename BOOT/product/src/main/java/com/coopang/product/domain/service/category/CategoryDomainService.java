package com.coopang.product.domain.service.category;

import com.coopang.product.application.request.category.CategoryDto;
import com.coopang.product.domain.entity.category.CategoryEntity;
import com.coopang.product.infrastructure.repository.category.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryDomainService {

    private final CategoryJpaRepository categoryJpaRepository;

    @Transactional
    public CategoryEntity create(CategoryDto categoryDto) {
        CategoryEntity categoryEntity = CategoryEntity.create(categoryDto.getCategoryId(), categoryDto.getCategoryName());
        return categoryJpaRepository.save(categoryEntity);
    }
}
