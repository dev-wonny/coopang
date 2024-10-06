package com.coopang.product.presentation.controller;

import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.product.application.request.ProductStockDto;
import com.coopang.product.application.request.ProductStockHistoryDto;
import com.coopang.product.application.service.ProductService;
import com.coopang.product.presentation.request.AddStockRequest;
import com.coopang.product.presentation.request.UpdateStockHistoryRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ProductStockHistoryController API", description = "ProductStockHistoryController API")
@RestController
@RequestMapping("/product-stock-histories/v1/product")
@RequiredArgsConstructor
public class ProductStockHistoryController {

    private final ProductService productService;
    private final ModelMapperConfig mapperConfig;

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";


    @Secured({"ROLE_MASTER","ROLE_HUB_MANAGER","ROLE_COMPANY"})
    @GetMapping("/{productId}/product-stock-history")
    public ResponseEntity<?> getStockHistoriesByProductId(@PathVariable UUID productId, Pageable pageable) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured({"ROLE_MASTER","ROLE_HUB_MANAGER","ROLE_COMPANY"})
    @GetMapping("/{productId}/product-stock-history")
    public ResponseEntity<?> getStockHistoriesByProductIdWithCondition(@PathVariable UUID productId, Pageable pageable) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured({"ROLE_MASTER","ROLE_HUB_MANAGER","ROLE_COMPANY"})
    @PutMapping("/{productId}/product-stock-history/{productStockHistoryId}")
    public ResponseEntity<?> updateProductStockHistory(@PathVariable UUID productId, @PathVariable UUID productStockHistoryId,
        @RequestBody @Valid UpdateStockHistoryRequest request) {

        ProductStockHistoryDto productStockHistoryDto = mapperConfig.strictMapper().map(request, ProductStockHistoryDto.class);
        productService.updateProductStockHistory(productStockHistoryDto,productId,productStockHistoryId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({"ROLE_MASTER","ROLE_HUB_MANAGER","ROLE_COMPANY"})
    @DeleteMapping("/{productId}/product-stock-history/{productStockHistoryId")
    public ResponseEntity<?> deleteProductStockHistoryById(@PathVariable UUID productId, @PathVariable UUID productStockHistoryId,
        @RequestHeader(USER_ID_HEADER) UUID userId,
        @RequestHeader(USER_ROLE_HEADER) String role) {

        productService.deleteProductStockHistory(productId,productStockHistoryId,userId,role);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
