package com.coopang.product.infrastructure.entity;

import com.coopang.apidata.jpa.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "p_product_stock_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
@SQLRestriction("is_deleted = false")
public class ProductStockHistoryEntity extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "product_stock_history_id", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID productStockHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_stock_id",nullable = false)
    private ProductStockEntity productStockEntity;

    @Column(name = "order_id")
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_stock_history_change_type",nullable = false)
    private ProductStockHistoryChangeType productStockHistoryChangeType;

    @Column(name = "product_stock_history_change_quantity",nullable = false)
    private int productStockHistoryChangeQuantity;

    @Column(name = "product_stock_history_previous_quantity",nullable = false)
    private int productStockHistoryPreviousQuantity;

    @Column(name = "product_stock_history_current_quantity",nullable = false)
    private int productStockHistoryCurrentQuantity;

    @Column(name = "product_stock_history_additional_info")
    private String productStockHistoryAdditionalInfo;

    public void addProductStockEntity(ProductStockEntity productStockEntity) {
        this.productStockEntity = productStockEntity;
    }

    @Builder(access = AccessLevel.PRIVATE)
    public ProductStockHistoryEntity(ProductStockEntity productStockEntity, UUID orderId,
        ProductStockHistoryChangeType productStockHistoryChangeType,
        int productStockHistoryChangeQuantity, int productStockHistoryPreviousQuantity,int productStockHistoryCurrentQuantity,
        String productStockHistoryAdditionalInfo) {
        this.productStockEntity = productStockEntity;
        this.orderId = orderId;
        this.productStockHistoryChangeType = productStockHistoryChangeType;
        this.productStockHistoryChangeQuantity = productStockHistoryChangeQuantity;
        this.productStockHistoryPreviousQuantity = productStockHistoryPreviousQuantity;
        this.productStockHistoryCurrentQuantity = productStockHistoryCurrentQuantity;
        this.productStockHistoryAdditionalInfo = productStockHistoryAdditionalInfo;
    }

    public static ProductStockHistoryEntity create(ProductStockEntity productStockEntity, UUID orderId,
        ProductStockHistoryChangeType productStockHistoryChangeType,
        int productStockHistoryChangeQuantity, int productStockHistoryPreviousQuantity, int productStockHistoryCurrentQuantity,
        String productStockHistoryAdditionalInfo)
    {
        return ProductStockHistoryEntity.builder()
            .productStockEntity(productStockEntity)
            .orderId(orderId)
            .productStockHistoryChangeType(productStockHistoryChangeType)
            .productStockHistoryChangeQuantity(productStockHistoryChangeQuantity)
            .productStockHistoryPreviousQuantity(productStockHistoryPreviousQuantity)
            .productStockHistoryCurrentQuantity(productStockHistoryCurrentQuantity)
            .productStockHistoryAdditionalInfo(productStockHistoryAdditionalInfo)
            .build();
    }
}
