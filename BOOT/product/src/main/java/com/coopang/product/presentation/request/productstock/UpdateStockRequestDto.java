package com.coopang.product.presentation.request.productstock;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStockRequestDto {
    @PositiveOrZero(message = "Product Stock must be positive")
    private int amount;
    private UUID orderId;
}
