package com.coopang.product.domain.entitiy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
public class CategoryEntity {

    @Id
    @UuidGenerator
    @Column(name = "category_id", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID categoryId;

    @Column(nullable = false,name = "category_name")
    private String categoryName;

    @Column(nullable = false,name = "is_deleted")
    private boolean isDeleted = false;
}
