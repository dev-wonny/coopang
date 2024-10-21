package com.coopang.product.presentation.request.productStock;

import jakarta.validation.constraints.PositiveOrZero;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateStockRequest {

    @PositiveOrZero(message = "Product Stock must be positive")
    private int amount;
    private UUID orderId;
}
