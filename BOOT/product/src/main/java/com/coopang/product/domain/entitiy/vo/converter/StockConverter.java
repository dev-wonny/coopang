package com.coopang.product.domain.entitiy.vo.converter;

import com.coopang.product.domain.entitiy.vo.ProductStock;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StockConverter implements AttributeConverter<ProductStock, Integer>{

    @Override
    public Integer convertToDatabaseColumn(ProductStock productStock) {
        return productStock.getValue();
    }

    @Override
    public ProductStock convertToEntityAttribute(Integer integer) {
        return new ProductStock(integer);
    }
}
