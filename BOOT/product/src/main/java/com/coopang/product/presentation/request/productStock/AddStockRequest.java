package com.coopang.product.presentation.request.productStock;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddStockRequest {
    @PositiveOrZero(message = "Product Stock must be positive")
    private int amount;
}
