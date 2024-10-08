package com.coopang.product.application.service.product;

import com.coopang.apicommunication.kafka.message.CompleteProduct;
import com.coopang.apicommunication.kafka.message.ErrorProduct;
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
import com.coopang.product.domain.repository.product.ProductRepository;
import com.coopang.product.domain.service.product.ProductDomainService;
import com.coopang.product.infrastructure.repository.category.CategoryJpaRepository;
import com.coopang.product.infrastructure.repository.productStock.ProductStockJpaRepository;
import com.coopang.product.presentation.request.product.BaseSearchCondition;
import com.coopang.product.presentation.request.product.ProductSearchCondition;
import com.coopang.product.presentation.request.productStockHistory.ProductStockHistorySearchCondition;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    //상품의 재고 수량을 증가시키는 함수
    @Transactional
    public void addProductStock(UUID productId, ProductStockDto productStockDto) {

        int amount = productStockDto.getAmount();

        //비관적 락을 이용하여 조회 -> 다른 트랜잭션은 읽기와 쓰기 불가능
        ProductStockEntity productStockEntity = productRepository.findAndLockProductStock(productId);

        int previousStock = productStockEntity.getProductStock().getValue();

        try {
            productStockEntity.increaseStock(amount);
            ProductStockHistoryEntity stockHistory = ProductStockHistoryEntity.create(null,productStockEntity,productStockDto.getOrderId(), ProductStockHistoryChangeType.INCREASE,
                amount,previousStock,productStockEntity.getProductStock().getValue(),"Increase");

            productStockEntity.addStockHistory(stockHistory);
            productStockJpaRepository.save(productStockEntity);
        }catch (IllegalArgumentException e)
        {
            log.error(e.getMessage());
            throw new IllegalArgumentException("재고의 수량은 음수가 될 수 없습니다.");
        }

    }

    //상품의 재고 수량을 감소하는 함수 - 비관적 락 사
    @Transactional
    public void reduceProductStock(UUID productId, ProductStockDto productStockDto) {
        int amount = productStockDto.getAmount();
        //비관적 락을 이용하여 조회 -> 다른 트랜잭션은 읽기와 쓰기 불가능
        ProductStockEntity productStockEntity = productRepository.findAndLockProductStock(productId);

        int previousStock = productStockEntity.getProductStock().getValue();

        try {
            //재고 수량 감소
            productStockEntity.decreaseStock(amount);

            //재고 기록 등록
            ProductStockHistoryEntity stockHistory = ProductStockHistoryEntity.create(null,productStockEntity,productStockDto.getOrderId(), ProductStockHistoryChangeType.DECREASE,
                amount,previousStock,productStockEntity.getProductStock().getValue(),"Decrease");

            //재고 기록과 재고와 연결
            productStockEntity.addStockHistory(stockHistory);
            productStockJpaRepository.save(productStockEntity);

            try {

                ProductEntity productEntity = findByProductId(productId);
                //재고수량 차감 완료 메시지 생성 및 주문 서버에 send
                CompleteProduct completeProduct = new CompleteProduct();
                completeProduct.setOrderId(productStockDto.getOrderId());
                completeProduct.setCompanyId(productEntity.getCompanyId());
                String completedMessage = objectMapper.writeValueAsString(completeProduct);
                kafkaTemplate.send("complete_product",completedMessage);
                log.info("complete_product kafka sent " + completedMessage);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }

        }
        catch (IllegalArgumentException e)
        {
            //재고 수량 부족 시 주문 서버에 error product 메시지 요청
            log.error(e.getMessage());
            sendOutOfStockMessage(productStockDto.getOrderId());
            throw new IllegalArgumentException("주문 수량보다 재고 수량이 부족합니다.");
        }
    }

    //재고기록 논리적으로 삭제하는 함수
    @Transactional
    public void deleteProductStockHistory(UUID productId, UUID productStockHistoryId) {
        
        ProductEntity productEntity = findByProductId(productId);

        ProductStockEntity productStockEntity = productEntity.getProductStockEntity();
        ProductStockHistoryEntity productStockHistoryEntity = findStockHistoryById(
            productStockHistoryId, productStockEntity);

        productStockHistoryEntity.setDeleted(true);
        
    }

    //특정 상품의 재고 기록 엔티티를 찾는 함수
    private static ProductStockHistoryEntity findStockHistoryById(UUID productStockHistoryId,
        ProductStockEntity productStockEntity) {
        return productStockEntity.getProductStockHistories().stream().
            filter(entity -> entity.getProductStockHistoryId().equals(productStockHistoryId))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품재고기록입니다."));
    }

    //특정 상품의 재고 기록을 변경하는 함수
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

    //특정 상품의 재고기록들을 모두 조회하는 함수 (페이징, 정렬기능 지원)
    @Transactional(readOnly = true)
    public Page<ProductStockHistoryResponseDto> getStockHistoriesByProductId(UUID productId, Pageable pageable) {

        return productRepository.getProductStockHistoryByProductId(productId, pageable).map(ProductStockHistoryResponseDto::of);

    }

    //특정상품의 재고기록들을 키워드 혹은 날짜별로 조회하는 함수
    @Transactional(readOnly = true)
    public Page<ProductStockHistoryResponseDto> getStockHistoriesByProductIdWithCondition(ProductStockHistorySearchCondition condition, UUID productId, Pageable pageable) {

        return productRepository.searchProductStockHistoryByProductId(condition,productId,pageable).map(ProductStockHistoryResponseDto::of);
    }

    //주문 수량이 재고 수량보다 많을 경우 에러메시지 주문서버에 전달
    private void sendOutOfStockMessage(UUID orderId)
    {
        try{
            ErrorProduct errorProduct = new ErrorProduct();
            errorProduct.setOrderId(orderId);
            errorProduct.setErrorMessage("재고수량이 부족합니다.");
            String errorMessage = objectMapper.writeValueAsString(errorProduct);
            kafkaTemplate.send("error_product",errorMessage);

        }catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 잔액 부족/ 결제실패에 대한 상품 수량 롤백
     */
    @Transactional
    public void rollbackProduct(UUID orderId,UUID productId,int orderQuantity){

        //비관적 락을 이용하여 조회 -> 다른 트랜잭션은 읽기와 쓰기 불가능
        ProductStockEntity productStockEntity = productRepository.findAndLockProductStock(productId);

        int previousStock = productStockEntity.getProductStock().getValue();

        try {

            productStockEntity.increaseStock(orderQuantity);

            ProductStockHistoryEntity stockHistory = ProductStockHistoryEntity.create(null,productStockEntity,orderId, ProductStockHistoryChangeType.INCREASE,
                orderQuantity,previousStock,productStockEntity.getProductStock().getValue(),"Rollback Product");

            productStockEntity.addStockHistory(stockHistory);
        }catch (IllegalArgumentException e)
        {
            log.error(e.getMessage());
            throw new IllegalArgumentException("재고의 수량은 음수가 될 수 없습니다.");
        }
    }

    /**
     * 결제 취소 시 상품 수량 복구
     * @param orderId
     * @param productId
     * @param orderQuantity
     */
    @Transactional
    public void cancelProduct(UUID orderId, UUID productId, int orderQuantity)
    {

        //비관적 락을 이용하여 조회 -> 다른 트랜잭션은 읽기와 쓰기 불가능
        ProductStockEntity productStockEntity = productRepository.findAndLockProductStock(productId);

        int previousStock = productStockEntity.getProductStock().getValue();

        try {

            productStockEntity.increaseStock(orderQuantity);

            ProductStockHistoryEntity stockHistory = ProductStockHistoryEntity.create(null,productStockEntity,orderId, ProductStockHistoryChangeType.INCREASE,
                orderQuantity,previousStock,productStockEntity.getProductStock().getValue(),"Cancel Order and Product Stock increase");

            productStockEntity.addStockHistory(stockHistory);

        }catch (IllegalArgumentException e)
        {
            log.error(e.getMessage());
            throw new IllegalArgumentException("재고의 수량은 음수가 될 수 없습니다.");
        }

    }

    private ProductEntity findByProductId(UUID productId){
        return productRepository.getOneByProductId(productId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    private CategoryEntity findByCategoryId(UUID categoryId){
        return categoryJpaRepository.findById(categoryId).orElseThrow
            (() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
    }
}
