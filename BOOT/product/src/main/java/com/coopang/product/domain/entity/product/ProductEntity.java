package com.coopang.product.domain.entity.product;

import com.coopang.apidata.jpa.entity.base.BaseEntity;
import com.coopang.product.domain.entity.productstock.ProductStockEntity;
import com.coopang.product.domain.entity.category.CategoryEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Table(name = "p_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
public class ProductEntity extends BaseEntity {

    @Id
    @Column(name = "product_id", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID productId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    @OneToOne(mappedBy = "productEntity",cascade = CascadeType.ALL)
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
    private boolean isSale = true;

    public void addProductStockEntity(ProductStockEntity productStockEntity) {
        this.productStockEntity = productStockEntity;
    }

    @Builder(access = AccessLevel.PRIVATE)
    public ProductEntity(UUID productId,CategoryEntity categoryEntity,
        UUID companyId, String productName, double productPrice, boolean isHidden, boolean isSale) {
        this.productId = productId;
        this.categoryEntity = categoryEntity;
        this.companyId = companyId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.isHidden = isHidden;
        this.isSale = isSale;
    }

    public static ProductEntity create(UUID productId,CategoryEntity categoryEntity,
        UUID companyId, String productName, double productPrice, boolean isHidden, boolean isSale)
    {
        return ProductEntity.builder()
            .productId(productId != null ? productId : UUID.randomUUID())
            .categoryEntity(categoryEntity)
            .companyId(companyId)
            .productName(productName)
            .productPrice(productPrice)
            .isHidden(isHidden)
            .isSale(isSale)
            .build();
    }

    public void updateProduct(
         String productName, UUID companyId, CategoryEntity categoryEntity, double productPrice
    ){
        this.productName = productName;
        this.companyId = companyId;
        this.categoryEntity = categoryEntity;
        this.productPrice = productPrice;
    }

    public void activateHidden(){
        this.isHidden = true;
    }

    public void deactivateHidden(){
        this.isHidden = false;
    }

    public void activateSale(){
        this.isSale = true;
        this.isHidden = false;
    }

    public void deactivateSale(){
        this.isSale = false;
        this.isHidden = true;
    }
}
