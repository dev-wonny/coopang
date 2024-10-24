package com.coopang.product.presentation.controller.productstock;

import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.coredata.user.enums.UserRoleEnum.Authority;
import com.coopang.product.application.request.productstock.ProductStockDto;
import com.coopang.product.application.service.productstock.ProductStockService;
import com.coopang.product.presentation.request.productstock.AddStockRequestDto;
import com.coopang.product.presentation.request.productstock.UpdateStockRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "ProductStockController API", description = "ProductStockController API")
@RestController
@RequestMapping("/product-stocks/v1/product")
@RequiredArgsConstructor
public class ProductStockController {

    private final ProductStockService productStockService;
    private final ModelMapperConfig mapperConfig;

    @Secured({Authority.MASTER, Authority.COMPANY, Authority.HUB_MANAGER})
    @PatchMapping("/{productId}/restock")
    public ResponseEntity<Void> addProductStock(@PathVariable UUID productId, @Valid @RequestBody AddStockRequestDto addStockRequestDto) {
        final ProductStockDto productStockDto = mapperConfig.strictMapper().map(addStockRequestDto, ProductStockDto.class);
        productStockService.addProductStock(productId, productStockDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({Authority.MASTER, Authority.COMPANY, Authority.HUB_MANAGER})
    @PatchMapping("/{productId}/reduce")
    public ResponseEntity<Void> reduceProductStock(@PathVariable UUID productId, @Valid @RequestBody UpdateStockRequestDto updateStockRequestDto) {
        final ProductStockDto productStockDto = mapperConfig.strictMapper().map(updateStockRequestDto, ProductStockDto.class);
        productStockService.reduceProductStock(productId, productStockDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({Authority.MASTER, Authority.COMPANY, Authority.HUB_MANAGER})
    @PatchMapping("/{productId}/rollback-increase")
    public ResponseEntity<Void> rollbackProductStock(@PathVariable UUID productId, @Valid @RequestBody UpdateStockRequestDto updateStockRequestDto) {
        final ProductStockDto productStockDto = mapperConfig.strictMapper().map(updateStockRequestDto, ProductStockDto.class);
        productStockService.rollbackProduct(productStockDto.getOrderId(), productId, productStockDto.getAmount());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({Authority.MASTER, Authority.COMPANY, Authority.HUB_MANAGER})
    @PatchMapping("/{productId}/cancel-increase")
    public ResponseEntity<Void> cancelProductStock(@PathVariable UUID productId, @Valid @RequestBody UpdateStockRequestDto updateStockRequestDto) {
        final ProductStockDto productStockDto = mapperConfig.strictMapper().map(updateStockRequestDto, ProductStockDto.class);
        productStockService.cancelProduct(productStockDto.getOrderId(), productId, productStockDto.getAmount());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
