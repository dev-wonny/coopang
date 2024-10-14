package com.coopang.product.application.response.product;

import com.coopang.product.domain.entity.product.ProductEntity;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ProductResponseDto{

    private UUID productId;
    private UUID categoryId;
    private String categoryName;
    private UUID companyId;
    private String productName;
    private double productPrice;

    public static ProductResponseDto of(ProductEntity product) {
        return ProductResponseDto.builder()
            .productId(product.getProductId())
            .categoryId(product.getCategoryEntity().getCategoryId())
            .categoryName(product.getCategoryEntity().getCategoryName())
            .companyId(product.getCompanyId())
            .productName(product.getProductName())
            .productPrice(product.getProductPrice())
            .build();
    }
}

