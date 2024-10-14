package com.coopang.product.presentation.controller.productStock;

import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.apidata.application.user.enums.UserRoleEnum.Authority;
import com.coopang.product.application.request.productStock.ProductStockDto;
import com.coopang.product.application.service.product.ProductService;
import com.coopang.product.application.service.productstock.ProductStockService;
import com.coopang.product.presentation.request.productStock.AddStockRequestDto;
import com.coopang.product.presentation.request.productStock.UpdateStockRequestDto;
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

    private final ProductStockService productStockService;
    private final ModelMapperConfig mapperConfig;

    @Secured({Authority.MASTER, Authority.COMPANY, Authority.HUB_MANAGER})
    @PatchMapping("/{productId}/restock")
    public ResponseEntity<String> addProductStock(@PathVariable UUID productId,
                                                  @Valid @RequestBody AddStockRequestDto addStockRequestDto) {

        ProductStockDto productStockDto = mapperConfig.strictMapper().map(addStockRequestDto, ProductStockDto.class);

        productStockService.addProductStock(productId, productStockDto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({Authority.MASTER, Authority.COMPANY, Authority.HUB_MANAGER})
    @PatchMapping("/{productId}/reduce")
    public ResponseEntity<String> reduceProductStock(
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateStockRequestDto updateStockRequestDto) {

        ProductStockDto productStockDto = mapperConfig.strictMapper().map(updateStockRequestDto, ProductStockDto.class);

        productStockService.reduceProductStock(productId, productStockDto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({Authority.MASTER, Authority.COMPANY, Authority.HUB_MANAGER})
    @PatchMapping("/{productId}/rollback-increase")
    public ResponseEntity<String> rollbackProductStock(
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateStockRequestDto updateStockRequestDto) {

        ProductStockDto productStockDto = mapperConfig.strictMapper().map(updateStockRequestDto, ProductStockDto.class);

        productStockService.rollbackProduct(productStockDto.getOrderId(), productId, productStockDto.getAmount());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({Authority.MASTER, Authority.COMPANY, Authority.HUB_MANAGER})
    @PatchMapping("/{productId}/cancel-increase")
    public ResponseEntity<String> cancelProductStock(
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateStockRequestDto updateStockRequestDto) {

        ProductStockDto productStockDto = mapperConfig.strictMapper().map(updateStockRequestDto, ProductStockDto.class);

        productStockService.cancelProduct(productStockDto.getOrderId(), productId, productStockDto.getAmount());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
