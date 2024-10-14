package com.coopang.product.application.service.product;

import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.product.application.request.product.ProductDto;
import com.coopang.product.application.request.product.ProductHiddenAndSaleDto;
import com.coopang.product.application.response.product.ProductResponseDto;
import com.coopang.product.domain.entity.category.CategoryEntity;
import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.repository.product.ProductRepository;
import com.coopang.product.domain.service.product.ProductDomainService;
import com.coopang.product.infrastructure.repository.category.CategoryJpaRepository;
import com.coopang.product.infrastructure.repository.productStock.ProductStockJpaRepository;
import com.coopang.product.presentation.request.product.BaseSearchConditionDto;
import com.coopang.product.presentation.request.product.ProductSearchConditionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    //TODO : 내부통신 연결해야된다.
    private final ProductRepository productRepository;
    private final ProductDomainService productDomainService;
    private final CategoryJpaRepository categoryJpaRepository;
    private final ProductStockJpaRepository productStockJpaRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    //특정 상품 생성
    public ProductEntity createProduct(ProductDto productDto) {

        ProductEntity productEntity = productDomainService.create(productDto);

        return productEntity;
    }

    //특정 상품 조회 - 삭제 상품 미포함
    @Cacheable(value = "products", key = "#productId")
    @Transactional(readOnly = true)
    public ProductResponseDto getValidProductById(UUID productId) {

        ProductEntity productEntity = findByProductId(productId);

        return ProductResponseDto.of(productEntity);
    }

    //특정 상품 조회 - 삭제 상품 포함
    @Cacheable(value = "products", key = "#productId")
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(UUID productId) {

        ProductEntity productEntity = productRepository.getOneByProductIdWithCategory(productId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 상품입니다.")
        );

        return ProductResponseDto.of(productEntity);
    }

    //모든 상품 조회
    @Cacheable(value = "Allproducts", key = "#condition")
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProducts(BaseSearchConditionDto condition,String role,Pageable pageable) {

        /**
         * 마스터인 경우 모든 상품들을 봄
         */
        if(UserRoleEnum.isMaster(role)){
            return productRepository.findAll(pageable).map(ProductResponseDto::of);
        }else if(UserRoleEnum.isHubManager(role)){
            //허브에 소속된 업체들의 상품 리스트
            //TODO : 내부통신으로 업체리스트들을 조회
            return null;
        }else if(UserRoleEnum.isCompany(role)){
            //내 업체들의 상품들만 조회
            return productRepository.findAllWithStockAndCategoryByCompanyId(condition.getCompanyId(), pageable).map(ProductResponseDto::of);
        }else{
            return productRepository.findAllWithStockAndCategory(pageable).map(ProductResponseDto::of);
        }
    }

    //카테고리별로 상품 조회
    @Cacheable(value = "Allproducts", key = "#pageable.pageNumber+ '-' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProductWithCategory(UUID categoryId, Pageable pageable) {

        CategoryEntity categoryEntity = findByCategoryId(categoryId);
        Page<ProductEntity> productEntities = productRepository.findAllWithStockAndCategoryByCategoryId(categoryEntity.getCategoryId(), pageable);

        return productEntities.map(ProductResponseDto::of);
    }

    //특정 상품 변경
    @CacheEvict(value = "products", key = "#productId")
    @Transactional
    public void updateProduct(ProductDto productDto, UUID productId) {

        ProductEntity productEntity = findByProductId(productId);
        CategoryEntity categoryEntity = findByCategoryId(productDto.getCategoryId());
        productEntity.updateProduct(productDto.getProductName(),productDto.getCompanyId(),categoryEntity,productDto.getProductPrice());

    }

    //특정 상품에 대한 숨김처리
    @CacheEvict(value = "products", key = "#productId")
    @Transactional
    public void updateProductHidden(ProductHiddenAndSaleDto productHiddenAndSaleDto, UUID productId) {

        ProductEntity productEntity = findByProductId(productId);

        boolean isHidden = productHiddenAndSaleDto.getIsHidden();

        if(isHidden == true)
        {
            productEntity.activateHidden();
        }else{
            productEntity.deactivateHidden();
        }

    }

    //특정 상품에 대한 판매가능 여부 처리
    @CacheEvict(value = "products", key = "#productId")
    @Transactional
    public void updateProductSale(ProductHiddenAndSaleDto productHiddenAndSaleDto, UUID productId) {

        ProductEntity productEntity = findByProductId(productId);

        boolean isSale = productHiddenAndSaleDto.getIsSale();

        if(isSale == true)
        {
            productEntity.activateSale();
        }else
        {
            productEntity.deactivateSale();
        }

    }


    // 논리적 삭제 시 - 상품 숨김, 판매 불가능 처리
    @CacheEvict(value = "products", key = "#productId")
    @Transactional
    public void deleteProductById(UUID userId, String role, UUID productId) {

        ProductEntity productEntity = findByProductId(productId);

        productEntity.setDeleted(true);

        productEntity.deactivateSale();

    }

    //키워드로 상품 검색
    @Cacheable(value = "Allproducts", key = "#condition")
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> searchProduct(ProductSearchConditionDto searchCondition, Pageable pageable) {

        Page <ProductEntity> productEntities = productRepository.search(searchCondition, pageable);

        return productEntities.map(ProductResponseDto::of);
    }

    private ProductEntity findByProductId(UUID productId){
        return productRepository.getOneByProductIdWithCategory(productId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    private CategoryEntity findByCategoryId(UUID categoryId){
        return categoryJpaRepository.findById(categoryId).orElseThrow
            (() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
    }
}
