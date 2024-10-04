package com.coopang.product.domain.entity.vo.converter;

import com.coopang.product.domain.entity.vo.ProductStock;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.concurrent.atomic.AtomicInteger;

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
