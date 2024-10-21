package com.coopang.product.domain.entity.category;

import com.coopang.apidata.jpa.entity.base.BaseEntity;
import com.coopang.product.domain.entity.product.ProductEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "p_categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
public class CategoryEntity extends BaseEntity {

    @Id
    @Column(name = "category_id", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID categoryId;

    @Column(nullable = false,name = "category_name")
    private String categoryName;

    @OneToMany(mappedBy = "categoryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products;

    @Builder(access = AccessLevel.PRIVATE)
    public CategoryEntity(UUID categoryId,String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public static CategoryEntity create(UUID categoryId,String categoryName){
        return CategoryEntity.builder().categoryId(categoryId != null ? categoryId : UUID.randomUUID())
            .categoryName(categoryName).build();
    }

    public void updateCategoryName(String categoryName){
        this.categoryName = categoryName;
    }
}
