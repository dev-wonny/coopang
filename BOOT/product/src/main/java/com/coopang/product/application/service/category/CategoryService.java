package com.coopang.product.application.service.category;

import com.coopang.product.application.request.category.CategoryDto;
import com.coopang.product.application.response.category.CategoryResponseDto;
import com.coopang.product.domain.entity.category.CategoryEntity;
import com.coopang.product.domain.repository.category.CategoryRepository;
import com.coopang.product.domain.service.category.CategoryDomainService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryDomainService categoryDomainService;

    //카테고리 생성
    public CategoryResponseDto createCategory(CategoryDto categoryDto){

        CategoryEntity categoryEntity = categoryDomainService.create(categoryDto);

        return CategoryResponseDto.of(categoryEntity);
    }

    //카테고리 변경
    @Transactional
    public void updateCategory(UUID categoryId, CategoryDto categoryDto){

        CategoryEntity categoryEntity = findByCategoryId(categoryId);

        categoryEntity.updateCategoryName(categoryDto.getCategoryName());

    }

    //카테고리 삭제
    @Transactional
    public void deleteCategory(UUID categoryId){
        CategoryEntity categoryEntity = findByCategoryId(categoryId);
        categoryEntity.setDeleted(true);
    }

    //특정 카테고리 조회
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(UUID categoryId){

        return CategoryResponseDto.of(findByCategoryId(categoryId));
    }

    //모든 카테고리 조회(삭제 미포함)
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategory(){

        List<CategoryEntity> categoryEntities = categoryRepository.findAllByIsDeletedFalse();

        return categoryEntities.stream().map(CategoryResponseDto::of).toList();
    }

    //카테고리 조회하는 공통함수
    private CategoryEntity findByCategoryId(UUID categoryId){
        return categoryRepository.findByCategoryIdAndIsDeletedFalse(categoryId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")
        );
    }
}
