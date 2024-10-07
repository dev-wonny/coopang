package com.coopang.product.domain.entity.productStock;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ProductStockConverter implements AttributeConverter<ProductStock, Integer>{

    @Override
    public Integer convertToDatabaseColumn(ProductStock productStock) {
        return productStock.getValue();
    }

    @Override
    public ProductStock convertToEntityAttribute(Integer dbData) {
        return new ProductStock(dbData);
    }
}
