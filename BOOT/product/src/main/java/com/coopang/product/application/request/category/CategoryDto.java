package com.coopang.product.application.request.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {
    private UUID categoryId;
    private String categoryName;
}