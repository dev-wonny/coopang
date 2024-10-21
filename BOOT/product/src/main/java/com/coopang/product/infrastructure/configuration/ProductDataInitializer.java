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

    String[] fixedUuids = {
        "d48209d3-d50e-43d5-b053-6182a8707b59",
        "327f4b48-e276-4f57-8a47-3c7e7dec657f",
        "aa2d9e81-0055-4518-b1d2-045adc4ac73b",
        "e6e2784f-ff9e-44f5-98a7-1ae49cec997f",
        "1e9dd9a8-a708-4d8c-955a-6c9753aae6da",
        "f5a5cfd8-899b-49a8-8702-7c2008f8c4ec",
        "434041b3-bc40-4ec7-a1d8-f4fe3992819e",
        "aae99f4f-f1ee-4a10-80da-b21b9e6aa191",
        "e140faa0-4e99-4438-8576-9d0c27052bd3",
        "b4beb137-a459-4979-87d9-d180322f50d1",
        "51bf0b38-83e9-4ab6-af73-ba77acf27525",
        "b8064569-4bf5-4778-97ed-d7757dc8ffb2",
        "699dbac0-7d98-4c29-8a7f-1dd0ef4099de",
        "df1653c2-125a-41f8-a72b-66bb747bc512",
        "38f97c32-a1f9-4357-9bcb-f2760d4e6c8a",
        "50bd1324-7f54-4909-8852-d9df39189b5b",
        "a04697ce-e0ce-4d24-ba84-1a4b0ab21882",
        "4efb8dc8-e4c3-4519-8e46-57a3b297c434",
        "6f2ad1f8-9b5c-4110-8f05-dd733afce69c",
        "b84d6885-6153-4953-82db-4be82640fbc0",
        "14eb8025-78ef-4f39-92d0-140ebd903f98",
        "9607f7b4-46fc-406a-a325-63eda0aca71c",
        "f05f33f2-1bca-4805-a9f2-12055167c86c",
        "64004ac1-765a-4d3c-8b39-6a769d0ad8bd",
        "a72df252-054f-4b92-b691-3e0a6405bb56",
        "cc0197ca-e936-42f6-8e88-48a3ec2a1d5c",
        "2011a66c-3afd-47be-b23d-9660e8bfa2c8",
        "0f5f5104-6bca-44f3-8fd2-103702467e8c",
        "dcd39610-0adf-4581-bc18-bf6c84cde793",
        "b146ebba-0313-4bae-bc8d-bab3cbec3099",
        "6e53b9a6-6ea6-437f-8f4d-52e376ab9310",
        "600a83cf-933a-480b-8c45-897b923e2458",
        "98455832-9e2a-42fe-b9d6-400cd7e78a8e",
        "da5d82a2-e0db-413a-b608-f20327666574",
        "eb142a69-f8f6-4d55-a64b-e0c4b68c1b57",
        "cd1150e1-9da1-48b4-a670-46849d170096",
        "60201cb1-a739-4a4e-9a31-a00baf333f9e",
        "0c8fbc18-1969-42b7-87e6-196362b73d44",
        "f11f3cb2-9053-4f3b-ad8b-f6bb504d6542",
        "01f787d1-5810-4d15-8a37-ef793ae17457",
        "24d2bffc-7702-432d-acff-1f260c9b289d",
        "035160ed-4b52-4ea5-bb22-ac50c4fb5bdb",
        "7d884040-312e-4a17-8610-2e9ad389a455",
        "739ef34f-43aa-4a7a-9f76-ef006a7f8146",
        "be3bbe7e-2e0a-4bd4-afcd-a7cabcd4385b",
        "d440b07e-9033-4175-84fa-c1010dd2778a",
        "6efea0bb-1031-4f77-864a-1067735fc2cf",
        "07ad655d-7f54-4d7c-8171-0b5427d650f1",
        "cac115ef-6cf0-4f8c-b11b-3bd474bbdcc5",
        "4ca7f447-c90d-4bf2-8681-8686c0ca6531",
        "4497a40e-dcf0-4a14-ac2c-4a403639b8fc",
        "936ee2b5-666e-4ea8-afdb-e0bd82b31fc3",
        "daa9f8ad-26b1-4acf-acd2-b4bdca5f22fa",
        "1eb4a5a3-1ce7-4fb9-918f-a7cafa3d9ee4",
        "c53a8835-56a8-4f3f-afee-cd70f9130734",
        "5bd667d3-ca27-4415-a297-2ccddce5d325",
        "49ec250f-8d06-4ab4-964c-5293b16375ac",
        "3ef6e443-26f8-45df-b433-a72f51b54cc4",
        "6bb39ce1-17e4-4c35-b587-3d30d605b3c4",
        "907db52c-75cd-4fcc-8437-75d770adcbee",
        "d9a597ae-eeaf-4765-9dda-e0ba54addad4",
        "a31edab1-9b67-4d27-a6b4-69e142d98f23",
        "00c2bae4-d491-4ba2-8383-a1653d34fa79",
        "28554967-6bf4-44cb-9d44-03116a027005",
        "3319444e-b272-44a2-adac-0c6d03e206f5",
        "6dfd9fae-b66e-4b17-84c4-74dd490bd3d3",
        "e1be21e3-38ef-48e0-b6b6-328e071f87fb",
        "4a4ccd0d-e49d-47df-9d05-68bd1fe7d913",
        "8d87cbe9-8aac-46d6-b3ae-b7f8fffb5c6d",
        "13d3bf06-19ab-497c-8cb0-9dccb9db98b7",
        "a4f81587-e0a7-483c-9009-7f78d05255bd",
        "78fe6c22-0573-43f5-a88e-928f723597df",
        "18ed4312-2647-4cee-b988-818145fe634a",
        "116554d9-c98d-405d-845d-324a6854e21d",
        "6d7ab2f1-0a66-425e-8930-48daa206e073",
        "5b2ac0c6-4779-4adf-8af0-0694e9bc670f",
        "3a5da999-79e5-49d8-b280-8f4250613232",
        "1f5e7ac4-80c9-479a-a638-995226f3074a"};

    // 고정된 UUID 배열에서 하나를 가져오는 메소드
    private UUID getNextFixedUuid() {
        if (uuidIndex >= fixedUuids.length) {
            throw new IllegalStateException("고정된 UUID가 부족합니다.");
        }
        return UUID.fromString(fixedUuids[uuidIndex++]);
    }
}
