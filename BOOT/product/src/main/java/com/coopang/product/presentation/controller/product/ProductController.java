package com.coopang.product.presentation.controller.product;

import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_USER_ID;
import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_USER_ROLE;

import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.coredata.user.enums.UserRoleEnum;
import com.coopang.coredata.user.enums.UserRoleEnum.Authority;
import com.coopang.product.application.request.product.ProductDto;
import com.coopang.product.application.request.product.ProductHiddenAndSaleDto;
import com.coopang.product.application.response.ProductWithStockResponseDto;
import com.coopang.product.application.response.product.ProductResponseDto;
import com.coopang.product.application.service.ProductWithStockAndHistoryService;
import com.coopang.product.application.service.ProductWithStockService;
import com.coopang.product.application.service.product.ProductPermissionValidator;
import com.coopang.product.application.service.product.ProductService;
import com.coopang.product.presentation.request.product.CreateProductRequestDto;
import com.coopang.product.presentation.request.product.ProductBaseSearchConditionDto;
import com.coopang.product.presentation.request.product.ProductSearchConditionDto;
import com.coopang.product.presentation.request.product.UpdateProductHiddenRequestDto;
import com.coopang.product.presentation.request.product.UpdateProductRequestDto;
import com.coopang.product.presentation.request.product.UpdateProductSaleRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "ProductController API", description = "ProductController API")
@RestController
@RequestMapping("/products/v1")
@Slf4j(topic = "ProductController")
@RequiredArgsConstructor
public class ProductController {

    private final ModelMapperConfig mapperConfig;

    private final ProductWithStockService productWithStockService;
    private final ProductWithStockAndHistoryService productWithStockAndHistoryService;
    private final ProductService productService;
    private final ProductPermissionValidator productPermissionValidator;

    @Secured({Authority.MASTER, Authority.COMPANY, Authority.HUB_MANAGER})
    @PostMapping("/product")
    public ResponseEntity<ProductResponseDto> createProduct(
        @RequestHeader(HEADER_USER_ROLE) String userRole,
        @RequestHeader(HEADER_USER_ID) String userId,
        @Valid @RequestBody CreateProductRequestDto createProductRequestDto) {

        validationHubOrCompany(userRole,userId,createProductRequestDto.getCompanyId());

        final ProductDto productDto = mapperConfig.strictMapper().map(createProductRequestDto, ProductDto.class);
        final ProductResponseDto productResponseDto = productWithStockAndHistoryService.createProductWithProductStockAndProductStockHistory(productDto);

        return new ResponseEntity<>(productResponseDto, HttpStatus.CREATED);
    }

    @Secured({Authority.MASTER, Authority.COMPANY, Authority.HUB_MANAGER})
    @PutMapping("/product/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(
        @RequestHeader(HEADER_USER_ROLE) String userRole,
        @RequestHeader(HEADER_USER_ID) String userId,
        @Valid @RequestBody UpdateProductRequestDto updateProductRequestDto,
        @PathVariable UUID productId) {

        validationHubOrCompany(userRole,userId,updateProductRequestDto.getCompanyId());
        ProductDto productDto = mapperConfig.strictMapper().map(updateProductRequestDto, ProductDto.class);
        productService.updateProduct(productDto, productId);
        ProductResponseDto productInfo = productService.getProductById(productId);

        return new ResponseEntity<>(productInfo,HttpStatus.OK);
    }

    @Secured({Authority.MASTER, Authority.COMPANY, Authority.HUB_MANAGER})
    @PatchMapping("/product/{productId}/hidden")
    public ResponseEntity<Void> updateProductHidden(
        @RequestHeader(HEADER_USER_ROLE) String userRole,
        @RequestHeader(HEADER_USER_ID) String userId,
        @Valid @RequestBody UpdateProductHiddenRequestDto request, @PathVariable UUID productId) {

        ProductHiddenAndSaleDto productHiddenAndSaleDto = mapperConfig.strictMapper().map(request, ProductHiddenAndSaleDto.class);
        productService.updateProductHidden(productHiddenAndSaleDto, productId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({Authority.MASTER, Authority.COMPANY, Authority.HUB_MANAGER})
    @PatchMapping("/product/{productId}/sale")
    public ResponseEntity<Void> updateProductSale(
        @RequestHeader(HEADER_USER_ROLE) String userRole,
        @RequestHeader(HEADER_USER_ID) String userId,
        @Valid @RequestBody UpdateProductSaleRequestDto request, @PathVariable UUID productId) {

        ProductHiddenAndSaleDto productHiddenAndSaleDto = mapperConfig.strictMapper()
            .map(request, ProductHiddenAndSaleDto.class);
        productService.updateProductSale(productHiddenAndSaleDto, productId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({Authority.MASTER, Authority.COMPANY, Authority.HUB_MANAGER})
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> deleteProduct(
        @RequestHeader(HEADER_USER_ROLE) String userRole,
        @RequestHeader(HEADER_USER_ID) String userId,
        @PathVariable UUID productId) {

        productService.deleteProductById(productId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/product")
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(@ModelAttribute ProductBaseSearchConditionDto condition, @RequestHeader(HEADER_USER_ROLE) String role, Pageable pageable) {

        Page<ProductResponseDto> productResponseDto;

        validateSearchCondition(role,condition);

        if (UserRoleEnum.isMaster(role)) {
            productResponseDto = productService.getAllProductsByMaster(pageable);
        } else if (UserRoleEnum.isCompany(role)) {
            productResponseDto = productService.getAllProductInCompany(condition, pageable);
        } else if (UserRoleEnum.isHubManager(role)) {
            productResponseDto = productService.getAllProductInHub(condition, pageable);
        } else {
            productResponseDto = productService.getAllProductByEvery(pageable);
        }

        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }

    @PostMapping("/product/search")
    public ResponseEntity<Page<ProductResponseDto>> searchProduct(
        @RequestBody ProductSearchConditionDto searchCondition,
        @RequestHeader(HEADER_USER_ROLE) String role, Pageable pageable) {

        //마스터인 경우 모든 상품을 봄
        if (UserRoleEnum.isMaster(role)) {
            searchCondition.setIsAbleToWatchDeleted(true);
        }

        Page<ProductResponseDto> products = productService.searchProduct(searchCondition, pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductWithStockResponseDto> getProductById(
        @RequestHeader(HEADER_USER_ROLE) String role, @PathVariable UUID productId) {
        ProductWithStockResponseDto productWithStockResponseDto;

        if (UserRoleEnum.isMaster(role)) {
            productWithStockResponseDto = productWithStockService.getProductWithStockById(
                productId);
        } else {
            productWithStockResponseDto = productWithStockService.getValidProductWithStockById(
                productId);
        }
        return new ResponseEntity<>(productWithStockResponseDto, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}/product")
    public ResponseEntity<Page<ProductResponseDto>> getProductWithCategory(
        @PathVariable UUID categoryId, Pageable pageable) {

        Page<ProductResponseDto> productResponseDtos = productService.getProductWithCategory(
            categoryId, pageable);

        return new ResponseEntity<>(productResponseDtos, HttpStatus.OK);
    }

    /**
     * 허브매니저와 업체관리자에 대한 권한 및 소속검증
     * @param userRole 권한
     * @param userId : login ID
     * @param targetId : companyId
     */
    private void validationHubOrCompany(String userRole,String userId,UUID targetId){
        if(UserRoleEnum.isHubManager(userRole)){
            productPermissionValidator.verifyCompanyOfHubManager(userRole,UUID.fromString(userId),targetId);
        }else{
            productPermissionValidator.verifyCompanyOfCompanyManager(userRole,UUID.fromString(userId),targetId);
        }
    }

    //TODO : 조금더 깔끔한 방법으로 변경해야됨
    //Search 시 hub, company 인 경우 companyId, hubId 검증
    private void validateSearchCondition(String role, ProductBaseSearchConditionDto condition) {
        if (UserRoleEnum.isCompany(role) && (condition == null || condition.getCompanyId() == null)) {
            throw new IllegalArgumentException("Company ID cannot be null for company role");
        }
        if (UserRoleEnum.isHubManager(role) && (condition == null || condition.getHubId() == null)) {
            throw new IllegalArgumentException("Hub ID cannot be null for hub manager role");
        }
    }
}
