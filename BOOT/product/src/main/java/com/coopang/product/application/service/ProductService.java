package com.coopang.product.application.service;

import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.product.application.request.ProductDto;
import com.coopang.product.application.request.ProductHiddenAndSaleDto;
import com.coopang.product.application.request.ProductStockDto;
import com.coopang.product.application.response.ProductResponseDto;
import com.coopang.product.domain.entity.CategoryEntity;
import com.coopang.product.domain.entity.ProductEntity;
import com.coopang.product.domain.entity.ProductStockEntity;
import com.coopang.product.domain.entity.ProductStockHistoryChangeType;
import com.coopang.product.domain.entity.ProductStockHistoryEntity;
import com.coopang.product.domain.repository.CategoryRepository;
import com.coopang.product.domain.repository.ProductRepository;
import com.coopang.product.domain.service.ProductDomainService;
import com.coopang.product.presentation.request.BaseSearchCondition;
import com.coopang.product.presentation.request.ProductSearchCondition;
import com.coopang.product.presentation.request.UpdateStockRequest;
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
    private final CategoryRepository categoryRepository;

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
        return categoryRepository.findById(categoryId).orElseThrow
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

        long delay = initialDelay;
        int retryCount = 0;

        boolean isUpdated = false;

        //낙관적락 실패 시 재시도 로직
        while(retryCount < maxRetryCount && isUpdated == false){
            try {
                productStockEntity.increaseStock(amount);

                ProductStockHistoryEntity stockHistory = ProductStockHistoryEntity.create(productStockEntity,null, ProductStockHistoryChangeType.INCREASE,
                    amount,previousStock,productStockEntity.getProductStock().getValue(),"Increase");

                productStockEntity.addStockHistory(stockHistory);

                isUpdated = true;
            }catch (IllegalArgumentException e){
                e.printStackTrace();
                throw new IllegalArgumentException("재고 수량이 음수가 될 수 없습니다.");
            }catch(OptimisticLockingFailureException e){
                retryCount++;

                if (retryCount >= maxRetryCount) {
                    throw new RuntimeException("재고 업데이트 중 낙관적 락 충돌이 발생했습니다. 최대 재시도 횟수를 초과했습니다.");
                }

                try {
                    Thread.sleep(delay);
                    delay *= 2; //지수 백오프 전략
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // 인터럽트 상태 복원
                }
            }
        }

    }

    @Transactional
    public void reduceProductStock(UUID productId, UpdateStockRequest updateStockRequest) {
        int amount = updateStockRequest.getAmount();

        ProductEntity productEntity = findByProductId(productId);
        ProductStockEntity productStockEntity = productEntity.getProductStockEntity();
        int previousStock = productStockEntity.getProductStock().getValue();

        long delay = initialDelay;
        int retryCount = 0;
        boolean isUpdated = false;

        //낙관적락 실패 시 재시도 로직
        while(retryCount < maxRetryCount && isUpdated == false){
            try {
                productStockEntity.decreaseStock(amount);

                ProductStockHistoryEntity stockHistory = ProductStockHistoryEntity.create(productStockEntity,updateStockRequest.getOrderId(), ProductStockHistoryChangeType.DECREASE,
                    amount,previousStock,productStockEntity.getProductStock().getValue(),"Decrease");

                productStockEntity.addStockHistory(stockHistory);

                isUpdated = true;
            }catch (IllegalArgumentException e){
                e.printStackTrace();
                throw new IllegalArgumentException("재고 수량이 부족합니다.");
            }catch(OptimisticLockingFailureException e){
                retryCount++;

                if (retryCount >= maxRetryCount) {
                    throw new RuntimeException("재고 업데이트 중 낙관적 락 충돌이 발생했습니다. 최대 재시도 횟수를 초과했습니다.");
                }

                try {
                    Thread.sleep(delay);
                    delay *= 2; //지수 백오프 전략
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // 인터럽트 상태 복원
                }
            }
        }
    }
}
