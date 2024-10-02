package com.coopang.product.application.request;

import java.util.UUID;
import lombok.Data;

@Data
public class ProductDto{

    private String productName;
    private UUID companyId;
    private UUID categoryId;
    private double productPrice;
    private int productStock;
}
