package com.coopang.product.application.request.productStock;

import java.util.UUID;
import lombok.Data;

@Data
public class ProductStockDto {
    private int amount;
    private UUID orderId;
}
