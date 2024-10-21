package com.coopang.product.application.response.category;

import com.coopang.product.domain.entity.category.CategoryEntity;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CategoryResponseDto {

    private UUID categoryId;
    private String categoryName;

    public static CategoryResponseDto of(CategoryEntity category) {
        return CategoryResponseDto.builder().categoryId(category.getCategoryId())
            .categoryName(category.getCategoryName())
            .build();
    }
}
