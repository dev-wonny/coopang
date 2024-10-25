package com.coopang.product.presentation.request.productstock;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddStockRequestDto {
    @PositiveOrZero(message = "Product Stock must be positive")
    private int amount;
}
