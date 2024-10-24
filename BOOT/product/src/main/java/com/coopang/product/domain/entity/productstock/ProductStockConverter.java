package com.coopang.product.domain.entity.productstock;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ProductStockConverter implements AttributeConverter<ProductStock, Integer>{

    @Override
    public Integer convertToDatabaseColumn(ProductStock productStock) {
        return productStock.getCurrentStockQuantity();
    }

    @Override
    public ProductStock convertToEntityAttribute(Integer dbData) {
        return new ProductStock(dbData);
    }
}
