package com.coopang.product.presentation.controller.productStock;

import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.product.application.request.productStock.ProductStockDto;
import com.coopang.product.application.service.product.ProductService;
import com.coopang.product.presentation.request.productStock.AddStockRequest;
import com.coopang.product.presentation.request.productStock.UpdateStockRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    public ResponseEntity<String> addProductStock(@PathVariable UUID productId,
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

        ProductStockDto productStockDto = mapperConfig.strictMapper().map(updateStockRequest, ProductStockDto.class);

        productService.reduceProductStock(productId,productStockDto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({"ROLE_MASTER","ROLE_HUB_MANAGER","ROLE_COMPANY"})
    @PatchMapping("/{productId}/rollback-increase")
    public ResponseEntity<String> rollbackProductStock(
        @PathVariable UUID productId,
        @Valid @RequestBody UpdateStockRequest updateStockRequest) {

        ProductStockDto productStockDto = mapperConfig.strictMapper().map(updateStockRequest, ProductStockDto.class);

        productService.rollbackProduct(productStockDto.getOrderId(),productId,productStockDto.getAmount());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @Secured({"ROLE_MASTER","ROLE_HUB_MANAGER","ROLE_COMPANY"})
    @PatchMapping("/{productId}/cancel-increase")
    public ResponseEntity<String> cancelProductStock(
        @PathVariable UUID productId,
        @Valid @RequestBody UpdateStockRequest updateStockRequest) {

        ProductStockDto productStockDto = mapperConfig.strictMapper().map(updateStockRequest, ProductStockDto.class);

        productService.cancelProduct(productStockDto.getOrderId(),productId,productStockDto.getAmount());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
