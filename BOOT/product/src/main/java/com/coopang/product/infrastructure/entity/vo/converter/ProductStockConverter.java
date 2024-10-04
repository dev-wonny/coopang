package com.coopang.product.infrastructure.entity.vo.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.concurrent.atomic.AtomicInteger;

@Converter(autoApply = true)
public class ProductStockConverter implements AttributeConverter<AtomicInteger, Integer>{

    @Override
    public Integer convertToDatabaseColumn(AtomicInteger attribute) {
        return attribute.get();
    }

    @Override
    public AtomicInteger convertToEntityAttribute(Integer dbData) {
        return new AtomicInteger(dbData);
    }
}
