package com.coopang.product.domain.entitiy;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Entity
@Table(name = "p_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class ProductStockHistoryEntity {

    @Id
    @UuidGenerator
    @Column(name = "product_stock_history_id", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID productStockHistoryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false,cascade = CascadeType.ALL)
    @JoinColumn(name = "product_stock_id",nullable = false)
    private ProductStockEntity productStockEntity;

    @Column(name = "order_id")
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_stock_history_change_type",nullable = false)
    private ProductStockHistoryChangeType productStockHistoryChangeType;

    @Column(name = "product_stock_history_change_quantity",nullable = false)
    private int product_stock_history_change_quantity;

    @Column(name = "product_stock_history_current_quantity",nullable = false)
    private int productStockHistoryCurrentQuantity;

    @Column(name = "product_stock_history_additional_info",nullable = false)
    private String productStockHistoryAdditionalInfo;

    @Column(name = "is_deleted",nullable = false)
    private boolean isDeleted = false;
}
