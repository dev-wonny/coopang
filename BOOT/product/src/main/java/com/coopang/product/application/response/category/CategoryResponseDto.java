package com.coopang.product.application.response.category;

import com.coopang.product.domain.entity.category.CategoryEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CategoryResponseDto {
    private UUID categoryId;
    private String categoryName;

    public static CategoryResponseDto from(CategoryEntity category) {
        return CategoryResponseDto.builder()
            .categoryId(category.getCategoryId())
            .categoryName(category.getCategoryName())
            .build();
    }
}