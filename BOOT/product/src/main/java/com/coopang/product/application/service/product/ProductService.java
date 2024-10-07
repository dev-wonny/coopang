package com.coopang.product.application.service.product;

import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.product.application.request.product.ProductDto;
import com.coopang.product.application.request.product.ProductHiddenAndSaleDto;
import com.coopang.product.application.request.productStock.ProductStockDto;
import com.coopang.product.application.request.ProductStockHistory.ProductStockHistoryDto;
import com.coopang.product.application.response.product.ProductResponseDto;
import com.coopang.product.application.response.ProductStockHistory.ProductStockHistoryResponseDto;
import com.coopang.product.domain.entity.category.CategoryEntity;
import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.entity.productStock.ProductStockEntity;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryChangeType;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryEntity;
import com.coopang.product.domain.repository.category.CategoryRepository;
import com.coopang.product.domain.repository.product.ProductRepository;
import com.coopang.product.domain.service.product.ProductDomainService;
import com.coopang.product.infrastructure.repository.category.CategoryJpaRepository;
import com.coopang.product.presentation.request.product.BaseSearchCondition;
import com.coopang.product.presentation.request.product.ProductSearchCondition;
import com.coopang.product.presentation.request.productStockHistory.ProductStockHistorySearchCondition;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Value("${stock.retry.maxRetryCount:3}")
    private int maxRetryCount;
    @Value("${stock.retry.initialDelay:50}")
    private int initialDelay;

    public ProductResponseDto createProduct(ProductDto productDto) {

        ProductEntity productEntity = productDomainService.create(productDto);

        return ProductResponseDto.of(productEntity);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(UUID productId) {

        ProductEntity productEntity = findByProductId(productId);

        return ProductResponseDto.of(productEntity);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProducts(BaseSearchCondition condition,String role,Pageable pageable) {

        /**
         * 마스터인 경우 모든 상품들을 봄
         */
        if(isMaster(role)){
            return productRepository.findAll(pageable).map(ProductResponseDto::of);
        }else if(isHubManager(role)){
            //허브에 소속된 업체들의 상품 리스트
            //TODO : 내부통신으로 업체리스트들을 조회
            return null;
        }else if(isCompany(role)){
            //내 업체들의 상품들만 조회
            return productRepository.findAllWithStockAndCategoryByCompanyId(condition.getCompanyId(), pageable).map(ProductResponseDto::of);
        }else{
            return productRepository.findAllWithStockAndCategory(pageable).map(ProductResponseDto::of);
        }
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProductWithCategory(UUID categoryId, Pageable pageable) {

        findByCategoryId(categoryId);
        Page<ProductEntity> productEntities = productRepository.findAllWithStockAndCategoryByCategoryId(categoryId, pageable);

        return productEntities.map(ProductResponseDto::of);
    }

    @Transactional
    public void updateProduct(ProductDto productDto, UUID productId) {

        ProductEntity productEntity = findByProductId(productId);
        CategoryEntity categoryEntity = findByCategoryId(productDto.getCategoryId());
        productEntity.updateProduct(productDto.getProductName(),productDto.getCompanyId(),categoryEntity,productDto.getProductPrice());

    }

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

    /***
     * 논리적 삭제 시
     * 상품 숨김, 판매 불가능 처리
     * @param productId
     */
    @Transactional
    public void deleteProductById(UUID userId, String role, UUID productId) {

        if(isCompany(role))
        {
           //TODO : 업체 관리자 조회(company와 통신)
        }else if(isHubManager(role))
        {
            //TODO : 허브 관리자 조회(hub와 통신)
        }

        ProductEntity productEntity = findByProductId(productId);

        productEntity.setDeleted(true);

        productEntity.deactivateSale();

    }

    private ProductEntity findByProductId(UUID productId){
        return productRepository.getOneByProductId(productId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    private CategoryEntity findByCategoryId(UUID categoryId){
        return categoryJpaRepository.findById(categoryId).orElseThrow
            (() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> searchProduct(ProductSearchCondition searchCondition, Pageable pageable) {

        Page <ProductEntity> productEntities = productRepository.search(searchCondition, pageable);

        return productEntities.map(ProductResponseDto::of);
    }

    private boolean isMaster(String role){
        return role.equals(UserRoleEnum.MASTER);
    }

    private boolean isHubManager(String role){
        return role.equals(UserRoleEnum.HUB_MANAGER);
    }

    private boolean isCompany(String role){
        return role.equals(UserRoleEnum.COMPANY);
    }

    @Transactional
    public void addProductStock(UUID productId, ProductStockDto productStockDto) {

        int amount = productStockDto.getAmount();

        ProductEntity productEntity = findByProductId(productId);
        ProductStockEntity productStockEntity = productEntity.getProductStockEntity();
        int previousStock = productStockEntity.getProductStock().getValue();

        retryStockUpdate(() -> {
            productStockEntity.increaseStock(amount);

            ProductStockHistoryEntity stockHistory = ProductStockHistoryEntity.create(null,productStockEntity,productStockDto.getOrderId(), ProductStockHistoryChangeType.INCREASE,
                amount,previousStock,productStockEntity.getProductStock().getValue(),"Increase");

            productStockEntity.addStockHistory(stockHistory);
        });
    }

    @Transactional
    public void reduceProductStock(UUID productId, ProductStockDto productStockDto) {
        int amount = productStockDto.getAmount();

        ProductEntity productEntity = findByProductId(productId);
        ProductStockEntity productStockEntity = productEntity.getProductStockEntity();
        int previousStock = productStockEntity.getProductStock().getValue();

        retryStockUpdate(() -> {
            productStockEntity.decreaseStock(amount);

            ProductStockHistoryEntity stockHistory = ProductStockHistoryEntity.create(null,productStockEntity,productStockDto.getOrderId(), ProductStockHistoryChangeType.DECREASE,
                amount,previousStock,productStockEntity.getProductStock().getValue(),"Decrease");

            productStockEntity.addStockHistory(stockHistory);
        });
    }

    @Transactional
    public void deleteProductStockHistory(UUID productId, UUID productStockHistoryId) {
        
        ProductEntity productEntity = findByProductId(productId);

        ProductStockEntity productStockEntity = productEntity.getProductStockEntity();
        ProductStockHistoryEntity productStockHistoryEntity = findStockHistoryById(
            productStockHistoryId, productStockEntity);

        productStockHistoryEntity.setDeleted(true);
        
    }

    private static ProductStockHistoryEntity findStockHistoryById(UUID productStockHistoryId,
        ProductStockEntity productStockEntity) {
        return productStockEntity.getProductStockHistories().stream().
            filter(entity -> entity.getProductStockHistoryId().equals(productStockHistoryId))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품재고기록입니다."));
    }

    @Transactional
    public void updateProductStockHistory(ProductStockHistoryDto productStockHistoryDto,UUID productId, UUID productStockHistoryId) {

        ProductEntity productEntity = findByProductId(productId);

        ProductStockEntity productStockEntity = productEntity.getProductStockEntity();
        ProductStockHistoryEntity productStockHistoryEntity = findStockHistoryById(
            productStockHistoryId, productStockEntity);

        productStockHistoryEntity.updateProductStockHistory(
            productStockHistoryDto.getProductStockHistoryChangeQuantity(),
            productStockHistoryDto.getProductStockHistoryPreviousQuantity(),
            productStockHistoryDto.getProductStockHistoryCurrentQuantity(),
            productStockHistoryDto.getProductStockHistoryAdditionalInfo(),
            productStockHistoryDto.getProductStockHistoryChangeType()
        );
    }

    @Transactional(readOnly = true)
    public Page<ProductStockHistoryResponseDto> getStockHistoriesByProductId(UUID productId, Pageable pageable) {

        return productRepository.getProductStockHistoryByProductId(productId, pageable).map(ProductStockHistoryResponseDto::of);

    }

    @Transactional(readOnly = true)
    public Page<ProductStockHistoryResponseDto> getStockHistoriesByProductIdWithCondition(ProductStockHistorySearchCondition condition, UUID productId, Pageable pageable) {

        return productRepository.searchProductStockHistoryByProductId(condition,productId,pageable).map(ProductStockHistoryResponseDto::of);
    }

    private void retryStockUpdate(Runnable stockUpdateOperation) {
        long delay = initialDelay;
        int retryCount = 0;
        boolean isUpdated = false;

        while (retryCount < maxRetryCount && !isUpdated) {
            try {
                stockUpdateOperation.run();
                isUpdated = true;
            }
            catch (IllegalArgumentException e)
            {
                log.error(e.getMessage());
            }
            catch (OptimisticLockingFailureException e) {
                retryCount++;
                if (retryCount >= maxRetryCount) {
                    throw new RuntimeException("재고 업데이트 중 낙관적 락 충돌이 발생했습니다. 최대 재시도 횟수를 초과했습니다.", e);
                }
                try {
                    Thread.sleep(delay);
                    delay *= 2; // 지수 백오프 전략
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // 인터럽트 상태 복원
                }
            }
        }
    }
}
