package com.coopang.product.presentation.request.category;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateCategoryRequestDto {

    @NotNull
    private String categoryName;
}
