package com.coopang.product.application.request;

import java.util.UUID;

public record ProductDto(
    String productName,
    UUID companyId,
    UUID categoryId,
    double productPrice,
    int productStock
) {

}
