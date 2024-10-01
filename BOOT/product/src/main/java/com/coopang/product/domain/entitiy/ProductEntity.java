package com.coopang.product.domain.entitiy;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;
import org.modelmapper.config.Configuration.AccessLevel;

@Getter
@Entity
@Table(name = "p_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class ProductEntity {

    @Id
    @UuidGenerator
    @Column(name = "product_id", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID productId;

    @OneToOne
    private CategoryEntity categoryEntity;

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ProductStockEntity productStockEntity;


    @Column(nullable = false,name = "company_id")
    private UUID companyId;

    @Column(nullable = false,name = "product_name")
    private String productName;

    @Column(nullable = false,name = "product_price")
    private double productPrice;

    @Column(nullable = false,name = "is_hidden")
    private boolean isHidden = false;

    @Column(nullable = false,name = "is_sale")
    private boolean isSale = false;

    @Column(nullable = false,name = "is_deleted")
    private boolean isDeleted = false;

}
