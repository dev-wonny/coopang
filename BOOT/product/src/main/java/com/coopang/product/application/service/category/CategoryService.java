package com.coopang.product.application.service.category;

import com.coopang.product.application.request.category.CategoryDto;
import com.coopang.product.application.response.category.CategoryResponseDto;
import com.coopang.product.domain.entity.category.CategoryEntity;
import com.coopang.product.domain.repository.category.CategoryRepository;
import com.coopang.product.domain.service.category.CategoryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryDomainService categoryDomainService;

    //카테고리 생성
    public CategoryResponseDto createCategory(CategoryDto categoryDto) {
        // 서비스 레이어에서 UUID 생성
        final UUID categoryId = categoryDto.getCategoryId() != null ? categoryDto.getCategoryId() : UUID.randomUUID();
        categoryDto.setCategoryId(categoryId);

        CategoryEntity categoryEntity = categoryDomainService.create(categoryDto);
        return CategoryResponseDto.from(categoryEntity);
    }

    //카테고리 변경
    @Transactional
    public void updateCategory(UUID categoryId, CategoryDto categoryDto) {
        CategoryEntity categoryEntity = findValidCategoryById(categoryId);
        categoryEntity.updateCategoryName(categoryDto.getCategoryName());
    }

    //카테고리 삭제
    @Transactional
    public void deleteCategory(UUID categoryId) {
        CategoryEntity categoryEntity = findValidCategoryById(categoryId);
        categoryEntity.setDeleted(true);
    }

    //특정 카테고리 조회
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(UUID categoryId) {
        CategoryEntity categoryEntity = findValidCategoryById(categoryId);
        return CategoryResponseDto.from(categoryEntity);
    }

    //모든 카테고리 조회(삭제 미포함)
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getCategoryList() {
        final List<CategoryEntity> categoryList = categoryRepository.findAllByIsDeletedFalse();
        return categoryList.stream()
            .map(CategoryResponseDto::from)
            .toList();
    }

    //카테고리 조회하는 공통함수
    private CategoryEntity findValidCategoryById(UUID categoryId) {
        return categoryRepository.findByCategoryIdAndIsDeletedFalse(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
    }
}
