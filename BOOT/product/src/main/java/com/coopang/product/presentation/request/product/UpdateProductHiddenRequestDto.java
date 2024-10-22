package com.coopang.product.presentation.request.product;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductHiddenRequestDto {
    @NotNull
    private boolean isHidden;
}
