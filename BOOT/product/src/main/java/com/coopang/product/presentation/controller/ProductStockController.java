package com.coopang.product.presentation.controller;

import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.product.application.request.ProductStockDto;
import com.coopang.product.application.service.ProductService;
import com.coopang.product.presentation.request.AddStockRequest;
import com.coopang.product.presentation.request.UpdateStockRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ProductStockController API", description = "ProductStockController API")
@RestController
@RequestMapping("/product-stocks/v1/product")
@RequiredArgsConstructor
public class ProductStockController {

    private final ProductService productService;
    private final ModelMapperConfig mapperConfig;

    @Secured({"ROLE_MASTER","ROLE_HUB_MANAGER","ROLE_COMPANY"})
    @PatchMapping("/{productId}/restock")
    public ResponseEntity<?> addProductStock(@PathVariable UUID productId,
        @Valid @RequestBody AddStockRequest addStockRequest) {

        ProductStockDto productStockDto = mapperConfig.strictMapper().map(addStockRequest, ProductStockDto.class);

        productService.addProductStock(productId,productStockDto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({"ROLE_MASTER","ROLE_HUB_MANAGER","ROLE_COMPANY"})
    @PatchMapping("/{productId}/reduce")
    public ResponseEntity<String> reduceProductStock(
        @PathVariable UUID productId,
        @Valid @RequestBody UpdateStockRequest updateStockRequest) {
        // 상품 재고 수량 변경 로직
        ProductStockDto productStockDto = mapperConfig.strictMapper().map(updateStockRequest, ProductStockDto.class);

        productService.reduceProductStock(productId,updateStockRequest);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({"ROLE_MASTER","ROLE_HUB_MANAGER","ROLE_COMPANY"})
    @PatchMapping("/{productId}/rollback-increase")
    public ResponseEntity<?> increaseProductStockByRollback(
        @PathVariable UUID productId,
        @PathVariable UUID productStockId,
        @RequestBody Map<String, Object> stockUpdateRequest) {
        // 상품 재고 수량 변경 로직

        return ResponseEntity.ok("Stock updated successfully");
    }

    @Secured({"ROLE_MASTER","ROLE_HUB_MANAGER","ROLE_COMPANY"})
    @PatchMapping("/{productId}/cancel-increase")
    public ResponseEntity<String> increaseProductStockByCancel(
        @PathVariable UUID productId,
        @PathVariable UUID productStockId,
        @RequestBody Map<String, Object> stockUpdateRequest) {
        // 상품 재고 수량 변경 로직

        return ResponseEntity.ok("Stock updated successfully");
    }

}
