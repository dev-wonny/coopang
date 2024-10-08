package com.coopang.product.domain.entity.productStock;

import com.coopang.apidata.jpa.entity.base.BaseEntity;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryEntity;
import com.coopang.product.domain.entity.product.ProductEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Table(name = "p_product_stocks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
@SQLRestriction("is_deleted = false")
public class ProductStockEntity extends BaseEntity {

    @Id
    @Column(name = "product_stock_id", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID productStockId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity productEntity;

    @Embedded
    private ProductStock productStock;

    @OneToMany(mappedBy = "productStockEntity", cascade = CascadeType.ALL)
    private List<ProductStockHistoryEntity> productStockHistories = new ArrayList<>();

    public void addStockHistory(ProductStockHistoryEntity stockHistory) {
        this.productStockHistories.add(stockHistory);
        stockHistory.addProductStockEntity(this);
    }

    @Builder(access = AccessLevel.PRIVATE)
    public ProductStockEntity(UUID productStockId,ProductEntity productEntity, ProductStock productStock) {
        this.productStockId = productStockId;
        this.productEntity = productEntity;
        this.productStock = productStock;
    }

    public static ProductStockEntity create(UUID productStockId,ProductEntity productEntity, ProductStock productStock) {
        return ProductStockEntity.builder()
            .productStockId(productStockId != null ? productStockId : UUID.randomUUID())
            .productEntity(productEntity)
            .productStock(productStock)
            .build();
    }

    public void increaseStock(int amount) {
        try {
            this.productStock.addStock(amount);
        }catch(IllegalArgumentException e)
        {
            throw e;
        }
    }

    public void decreaseStock(int amount) {
        try {
            this.productStock.reduceStock(amount);
        }catch(IllegalArgumentException e)
        {
            throw e;
        }
    }
}
