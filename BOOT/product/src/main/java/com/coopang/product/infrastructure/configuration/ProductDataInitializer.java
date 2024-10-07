package com.coopang.product.infrastructure.configuration;

import com.coopang.product.application.request.product.ProductDto;
import com.coopang.product.domain.entity.category.CategoryEntity;
import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.entity.productStock.ProductStock;
import com.coopang.product.domain.entity.productStock.ProductStockEntity;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryChangeType;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryEntity;
import com.coopang.product.domain.service.product.ProductDomainService;
import com.coopang.product.infrastructure.repository.category.CategoryJpaRepository;
import com.coopang.product.infrastructure.repository.product.ProductJpaRepository;
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
    private final ProductJpaRepository productJpaRepository;
    private int uuidIndex = 0;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        List<CategoryEntity> savedCategories = createCategories();

        List<ProductDto> productDtos = createProductTestDto(
            savedCategories);

        // 상품, 재고, 재고기록 등록
        productDtos.forEach(productDto -> {
            createProduct(getNextFixedUuid(),productDto);
        });
    }

    //상품 10개 재고 10개 재고기록 10개 생성
    @Transactional
    public ProductEntity createProduct(UUID id,ProductDto productDto) {

        //1. 상품 생성
        ProductEntity productEntity = productDtoToProductEntity(id,productDto);

        //2. 재고엔티티 생성
        ProductStock stock = new ProductStock(productDto.getProductStock());
        ProductStockEntity productStockEntity = ProductStockEntity.create(
            getNextFixedUuid(),
            productEntity,
            stock
        );
        //3. 재고 기록엔티티 생성
        ProductStockHistoryEntity productStockHistoryEntity = ProductStockHistoryEntity.create(
            getNextFixedUuid(),
            productStockEntity,
            null,
            ProductStockHistoryChangeType.INCREASE,
            stock.getValue(),
            0,
            stock.getValue(),
            null
        );
        //4. 연관관계 설정
        productEntity.addProductStockEntity(productStockEntity);
        productStockEntity.addStockHistory(productStockHistoryEntity);

        return productJpaRepository.save(productEntity);
    }

    private ProductEntity productDtoToProductEntity(UUID id,ProductDto productDto) {

        return ProductEntity.create(
            id,
            categoryRepository.findById(productDto.getCategoryId()).orElse(null),
            productDto.getCompanyId(),
            productDto.getProductName(),
            productDto.getProductPrice(),
            false,
            true
        );
    }


    //10개의 상품 생성
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


    //카테고리 20개 생성
    private List<CategoryEntity> createCategories() {

        String []cateogoryValues = new String[]{
            "식품" , "음료", "식자재","가공식품","의류","패션잡화","신발","생활용품","가전제품","디지털 기기",
            "화장품", "유아용품","건강","스포츠","가구","자동차용품","펫용품","취미","공구","도서"
        };

        List<CategoryEntity> categories = new ArrayList<>();

        for(String s : cateogoryValues)
        {
            categories.add(CategoryEntity.create(getNextFixedUuid(),s));
        }

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

    // 고정된 uuid
    String[] fixedUuids = {
        "3e864e21-15c8-47e1-b3b3-c669abf7f0fa",
        "40d78eb3-ca15-44ac-a5b5-182830615f4f",
        "e73eb204-8f6e-4c61-8a5f-5df1c0a2797e",
        "a96f086f-87cf-4c7d-99e8-1c31db14ca5e",
        "d55c8cdb-760d-4dd3-ac77-c30d2cbf787d",
        "4e1d9435-820a-432e-abe3-b7a11c0fbeea",
        "7a612c48-3c7c-4333-a54c-032400745cf6",
        "3a5ed5e5-8ee1-42c2-b201-9949a99f6b72",
        "0008e5b7-b5a6-41f2-8e3c-29156039a642",
        "249196ce-33df-4f86-a46d-9d42fead886b",
        "8fccf12a-13db-4e8e-9a2c-cbf85cb24a04",
        "ddab89d6-a024-43aa-b6a1-826110786686",
        "55de6337-556d-43c6-adbf-469c2cf2dea9",
        "e8c54e27-92cc-4780-a4d9-2ada1a577a34",
        "aa2a4b10-c91e-4993-8e28-a907602f13d2",
        "239f11df-4280-43b4-81a4-7139b31b071b",
        "3a1386db-8ad3-4cac-a234-31925ada139b",
        "ec397ca8-dfd0-4b94-912c-233265282316",
        "30ecd2e9-8dee-4478-b418-a7a293805772",
        "d8d5414e-7812-4b01-99e2-1055cbccf676",
        "0cfeddd6-e080-4a1f-b918-b80e0064cf8c",
        "bb659b6b-da76-4cd9-ae54-e3d5dc671910",
        "6b4f5a31-5345-45c7-a167-dd1fd2cd6ae8",
        "fdb75eed-390a-4aad-8bed-611829fba713",
        "5800e01b-5e60-4261-ade6-20953f7e9d24",
        "58e95489-8a76-4e46-84b5-ac7ce78e35ca",
        "48410e1f-69c2-41d6-bd6e-892329bff329",
        "ad1ee1d0-53fd-495f-9f35-2cdb2aa6be05",
        "6a26ed86-79f6-46c8-a830-239e9bd45350",
        "fd96ab07-3350-4298-8570-34ca189315ee",
        "afa5f951-02eb-4442-9fa1-363bc8dcec26",
        "0c433c56-4c5d-45f2-989a-8db15f55226a",
        "2e418446-0e43-4af9-b34a-beb1470a87cb",
        "9440bc94-26cb-4a7a-8668-371f19ace9dc",
        "bd1fa464-5b68-4d09-977c-015f3e9c2f6a",
        "b4c8a76c-df35-4066-8203-3df2455fcf88",
        "99f26f79-1ed5-4799-b7a0-058a00df55eb",
        "37d3078c-2a9a-485d-8a2f-09f5c6874e94",
        "06abaa95-2431-4ff5-8472-5bc17a16a5c3",
        "b320b1f4-197a-41de-bb68-b882fc0b2870",
        "531de8b3-2008-4f13-9511-7ee406c2e14a",
        "afa88bdc-0633-4c01-a220-5308ccb5e765",
        "0d8ca948-6881-4dd6-a4a9-79ebe2692d8d",
        "d2bcfe27-a27c-4efb-978a-22c5c674929c",
        "0f343a78-e7a9-47f1-a669-bb301acbc7db",
        "4b933781-8b93-4ebf-bfd1-4ca447cd9416",
        "1d954968-9cad-4526-a1df-5cce4aace3ae",
        "ef88eab0-66a3-43fd-894b-5e3d1bbfc348",
        "eb9169b3-b3d8-4f9b-8e31-95706711fd41",
        "bbb4b3d5-ccfe-467d-a7a5-b5e8a711230a",
        "cf004528-251f-4ced-89f1-2cb6d81c9b58",
        "53e4ad5c-0a53-446b-b31a-11416a4d364e",
        "bcdd0a57-cc4a-43ff-bb09-b6b9fb8c5d19",
        "e1e04dd1-ce0a-4b96-85e0-540f1f72e585",
        "ede7fea2-c4dc-4d8e-a5f2-70c010d80b93",
        "4541b728-7b2e-48e6-8bd3-b336303c74d2",
        "021d9eec-6bb7-496b-a869-9b27498ba188",
        "be322296-d5b1-42cb-9502-a8dc9c42db7f",
        "a6867398-dc56-4a8f-938c-581d597a7916",
        "4d7aff3d-a455-43d8-8675-eeaab76eb5d3",
        "c14b44a1-28f3-4c58-8700-b616ff141fc6",
        "ac9bcee5-709c-4f10-8b2c-d45418aed6de",
        "a698a623-aa8f-4137-9951-a407af4025ab"
    };

    // 고정된 UUID 배열에서 하나를 가져오는 메소드
    private UUID getNextFixedUuid() {
        if (uuidIndex >= fixedUuids.length) {
            throw new IllegalStateException("고정된 UUID가 부족합니다.");
        }
        return UUID.fromString(fixedUuids[uuidIndex++]);
    }
}
