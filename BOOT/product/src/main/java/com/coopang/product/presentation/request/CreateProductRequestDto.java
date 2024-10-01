package com.coopang.product.presentation.request;

import java.util.UUID;

public record CreateProductRequestDto(
    String productName,
    UUID companyId,
    UUID categoryId,
    double productPrice,
    int productStock
) {

}
