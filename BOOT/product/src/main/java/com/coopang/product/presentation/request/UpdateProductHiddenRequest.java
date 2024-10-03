package com.coopang.product.presentation.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductHiddenRequest {
    @NotNull
    private boolean isHidden;
}
