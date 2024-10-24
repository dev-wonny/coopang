package com.coopang.product.application.service.product;

import com.coopang.product.application.request.product.ProductBaseSearchConditionDto;
import com.coopang.product.application.request.product.ProductDto;
import com.coopang.product.application.request.product.ProductHiddenAndSaleDto;
import com.coopang.product.application.request.product.ProductSearchConditionDto;
import com.coopang.product.application.response.product.ProductResponseDto;
import com.coopang.product.application.service.feignclient.CompanyFeignClientService;
import com.coopang.product.domain.entity.category.CategoryEntity;
import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.repository.product.ProductRepository;
import com.coopang.product.domain.service.product.ProductDomainService;
import com.coopang.product.infrastructure.repository.category.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDomainService productDomainService;
    private final CategoryJpaRepository categoryJpaRepository;
    private final CompanyFeignClientService companyFeignClientService;

    //특정 상품 생성
    @CacheEvict(value = {"products", "allProducts"}, allEntries = true)
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
        ProductEntity productEntity = productRepository.getOneByProductIdWithCategory(productId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        return ProductResponseDto.of(productEntity);
    }

    //모든 상품 조회 - 마스터인 경우
    @Cacheable(value = "allProducts", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProductsByMaster(Pageable pageable) {

        return productRepository.findAll(pageable).map(ProductResponseDto::of);
    }

    //허브 관리자일 경우 소속된 업체들의 상품들 조회
    @Cacheable(value = "allProducts", key = "#condition.toString()+'-'+#pageable.pageNumber + '-' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProductInHub(ProductBaseSearchConditionDto condition, Pageable pageable) {
        UUID hubId = condition.getHubId();
        List<UUID> companyIds = companyFeignClientService.getCompanyIdsInFeignClient(hubId);
        return productRepository.findAllWithStockAndCategoryByCompanyIds(companyIds, pageable)
            .map(ProductResponseDto::of);
    }

    //업체 관리자일 경우 자신의 상품들만 조회
    @Cacheable(value = "allProducts", key = "#condition.toString()+'-'+#pageable.pageNumber + '-' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProductInCompany(
        ProductBaseSearchConditionDto condition, Pageable pageable) {
        return productRepository.findAllWithStockAndCategoryByCompanyId(condition.getCompanyId(), pageable).map(ProductResponseDto::of);
    }

    //모든 상품들을 조회
    @Cacheable(value = "allProducts", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProductByEvery(Pageable pageable) {
        return productRepository.findAllWithStockAndCategory(pageable).map(ProductResponseDto::of);
    }

    //카테고리별로 상품 조회
    @Cacheable(value = "allProducts", key = "#categoryId + '-' +#pageable.pageNumber+ '-' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProductWithCategory(UUID categoryId, Pageable pageable) {

        CategoryEntity categoryEntity = findByCategoryId(categoryId);
        Page<ProductEntity> productEntities = productRepository.findAllWithStockAndCategoryByCategoryId(
            categoryEntity.getCategoryId(), pageable);

        return productEntities.map(ProductResponseDto::of);
    }

    //특정 상품 변경
    @Caching(evict = {
        @CacheEvict(value = "products", allEntries = true),
        @CacheEvict(value = "allProducts", allEntries = true)
    })
    @Transactional
    public void updateProduct(ProductDto productDto, UUID productId) {

        ProductEntity productEntity = findByProductId(productId);
        CategoryEntity categoryEntity = findByCategoryId(productDto.getCategoryId());
        productEntity.updateProduct(
            productDto.getProductName()
            , productDto.getCompanyId()
            , categoryEntity
            , productDto.getProductPrice()
        );

    }

    //특정 상품에 대한 숨김처리
    @Caching(evict = {
        @CacheEvict(value = "products", allEntries = true),
        @CacheEvict(value = "allProducts", allEntries = true)
    })
    @Transactional
    public void updateProductHidden(ProductHiddenAndSaleDto productHiddenAndSaleDto, UUID productId) {

        ProductEntity productEntity = findByProductId(productId);

        if (productHiddenAndSaleDto.getIsHidden()) {
            productEntity.activateHidden();
        } else {
            productEntity.deactivateHidden();
        }
    }

    //특정 상품에 대한 판매가능 여부 처리
    @Caching(evict = {
        @CacheEvict(value = "products", allEntries = true),
        @CacheEvict(value = "allProducts", allEntries = true)
    })
    @Transactional
    public void updateProductSale(ProductHiddenAndSaleDto productHiddenAndSaleDto, UUID productId) {

        ProductEntity productEntity = findByProductId(productId);

        if (productHiddenAndSaleDto.getIsSale()) {
            productEntity.activateSale();
        } else {
            productEntity.deactivateSale();
        }
    }


    // 논리적 삭제 시 - 상품 숨김, 판매 불가능 처리
    @Caching(evict = {
        @CacheEvict(value = "products", allEntries = true),
        @CacheEvict(value = "allProducts", allEntries = true)
    })
    @Transactional
    public void deleteProductById(UUID productId) {

        ProductEntity productEntity = findByProductId(productId);
        productEntity.setDeleted(true);
        productEntity.deactivateSale();

    }

    //키워드로 상품 검색
    @Cacheable(value = "allProducts", key = "#searchCondition.toString() + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> searchProduct(ProductSearchConditionDto searchCondition,
                                                  Pageable pageable) {

        Page<ProductEntity> productEntities = productRepository.search(searchCondition, pageable);
        return productEntities.map(ProductResponseDto::of);
    }

    private ProductEntity findByProductId(UUID productId) {
        return productRepository.getOneByProductIdWithCategory(productId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    private CategoryEntity findByCategoryId(UUID categoryId) {
        return categoryJpaRepository.findById(categoryId).orElseThrow
            (() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
    }
}
