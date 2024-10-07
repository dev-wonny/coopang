package com.coopang.product.infrastructure.configuration;

import com.coopang.product.application.request.product.ProductDto;
import com.coopang.product.domain.entity.category.CategoryEntity;
import com.coopang.product.domain.service.product.ProductDomainService;
import com.coopang.product.infrastructure.repository.category.CategoryJpaRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
class ProductDataInitializer implements ApplicationRunner {

    private final CategoryJpaRepository categoryRepository;
    private final ProductDomainService productDomainService;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        List<CategoryEntity> savedCategories = createCategories();

        List<ProductDto> productDtos = createProductTestDto(
            savedCategories);

        // 상품, 재고, 재고기록 등록
        productDtos.forEach(productDto -> {
            productDomainService.create(productDto);
        });
    }

    private List<ProductDto> createProductTestDto(List<CategoryEntity> savedCategories) {

        List<ProductDto> productDtos = new ArrayList<>();

        ProductDto product1 = new ProductDto();
        product1.setProductName("바나나");
        product1.setCompanyId(UUID.randomUUID());
        product1.setCategoryId(findCategoryId(savedCategories, "식품"));
        product1.setProductPrice(3000.0);
        product1.setProductStock(0);
        productDtos.add(product1);

        ProductDto product2 = new ProductDto();
        product2.setProductName("콜라");
        product2.setCompanyId(UUID.randomUUID());
        product2.setCategoryId(findCategoryId(savedCategories, "음료"));
        product2.setProductPrice(1500.0);
        product2.setProductStock(0);
        productDtos.add(product2);

        ProductDto product3 = new ProductDto();
        product3.setProductName("밀가루");
        product3.setCompanyId(UUID.randomUUID());
        product3.setCategoryId(findCategoryId(savedCategories, "식자재"));
        product3.setProductPrice(2000.0);
        product3.setProductStock(50);
        productDtos.add(product3);

        ProductDto product4 = new ProductDto();
        product4.setProductName("라면");
        product4.setCompanyId(UUID.randomUUID());
        product4.setCategoryId(findCategoryId(savedCategories, "가공식품"));
        product4.setProductPrice(1000.0);
        product4.setProductStock(100);
        productDtos.add(product4);

        ProductDto product5 = new ProductDto();
        product5.setProductName("티셔츠");
        product5.setCompanyId(UUID.randomUUID());
        product5.setCategoryId(findCategoryId(savedCategories, "의류"));
        product5.setProductPrice(25000.0);
        product5.setProductStock(10);
        productDtos.add(product5);

        // 나머지 상품들도 같은 방식으로 설정
        ProductDto product6 = new ProductDto();
        product6.setProductName("지갑");
        product6.setCompanyId(UUID.randomUUID());
        product6.setCategoryId(findCategoryId(savedCategories, "패션잡화"));
        product6.setProductPrice(45000.0);
        product6.setProductStock(0);
        productDtos.add(product6);

        ProductDto product7 = new ProductDto();
        product7.setProductName("운동화");
        product7.setCompanyId(UUID.randomUUID());
        product7.setCategoryId(findCategoryId(savedCategories, "신발"));
        product7.setProductPrice(65000.0);
        product7.setProductStock(20);
        productDtos.add(product7);

        ProductDto product8 = new ProductDto();
        product8.setProductName("세제");
        product8.setCompanyId(UUID.randomUUID());
        product8.setCategoryId(findCategoryId(savedCategories, "생활용품"));
        product8.setProductPrice(5000.0);
        product8.setProductStock(0);
        productDtos.add(product8);

        ProductDto product9 = new ProductDto();
        product9.setProductName("전자레인지");
        product9.setCompanyId(UUID.randomUUID());
        product9.setCategoryId(findCategoryId(savedCategories, "가전제품"));
        product9.setProductPrice(120000.0);
        product9.setProductStock(0);
        productDtos.add(product9);

        ProductDto product10 = new ProductDto();
        product10.setProductName("노트북");
        product10.setCompanyId(UUID.randomUUID());
        product10.setCategoryId(findCategoryId(savedCategories, "디지털 기기"));
        product10.setProductPrice(1500000.0);
        product10.setProductStock(0);
        productDtos.add(product10);
        return productDtos;
    }

    private List<CategoryEntity> createCategories() {
        List<CategoryEntity> categories = Arrays.asList(
            new CategoryEntity("식품"),
            new CategoryEntity("음료"),
            new CategoryEntity("식자재"),
            new CategoryEntity("가공식품"),
            new CategoryEntity("의류"),
            new CategoryEntity("패션잡화"),
            new CategoryEntity("신발"),
            new CategoryEntity("생활용품"),
            new CategoryEntity("가전제품"),
            new CategoryEntity("디지털 기기"),
            new CategoryEntity("화장품/뷰티"),
            new CategoryEntity("유아용품"),
            new CategoryEntity("건강/의료"),
            new CategoryEntity("스포츠"),
            new CategoryEntity("가구"),
            new CategoryEntity("자동차용품"),
            new CategoryEntity("펫용품"),
            new CategoryEntity("취미/완구"),
            new CategoryEntity("공구"),
            new CategoryEntity("도서")
        );

        List<CategoryEntity> savedCategories = categoryRepository.saveAll(categories);
        return savedCategories;
    }

    // 카테고리 이름으로 카테고리를 찾는 메서드
    private UUID findCategoryId(List<CategoryEntity> categories, String categoryName) {
        return categories.stream()
            .filter(category -> category.getCategoryName().equals(categoryName))
            .map(CategoryEntity::getCategoryId)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryName));
    }

}
