package com.coopang.product.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequestDto{
    @NotBlank(message = "product Name is required")
    private String productName;
    @NotNull(message = "company Id is required")
    private UUID companyId;
    @NotNull(message = "category Id is required")
    private UUID categoryId;
    @Positive(message = "product Price must be positive")
    private double productPrice;
    @PositiveOrZero(message = "product Stock must be positive")
    private int productStock;
}
