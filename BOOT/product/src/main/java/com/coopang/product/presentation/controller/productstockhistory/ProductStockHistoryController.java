package com.coopang.product.presentation.controller.productstockhistory;

import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.coredata.user.enums.UserRoleEnum.Authority;
import com.coopang.product.application.request.Productstockhistory.ProductStockHistoryDto;
import com.coopang.product.application.response.Productstockhistory.ProductStockHistoryResponseDto;
import com.coopang.product.application.service.productstockhistory.ProductStockHistoryService;
import com.coopang.product.presentation.request.productstockhistory.ProductStockHistorySearchConditionDto;
import com.coopang.product.presentation.request.productstockhistory.UpdateStockHistoryRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ProductStockHistoryController API", description = "ProductStockHistoryController API")
@RestController
@RequestMapping("/product-stock-histories/v1/product")
@RequiredArgsConstructor
public class ProductStockHistoryController {

    private final ProductStockHistoryService productStockHistoryService;
    private final ModelMapperConfig mapperConfig;

    @Secured({Authority.MASTER,Authority.COMPANY,Authority.HUB_MANAGER})
    @GetMapping("/{productId}/product-stock-history")
    public ResponseEntity<Page<ProductStockHistoryResponseDto>> getStockHistoriesByProductId(
        @PathVariable UUID productId
        , Pageable pageable) {

        Page<ProductStockHistoryResponseDto> responseDtos = productStockHistoryService.getStockHistoriesByProductId(productId,pageable);
        return new ResponseEntity<>(responseDtos,HttpStatus.OK);
    }

    @Secured({Authority.MASTER,Authority.COMPANY,Authority.HUB_MANAGER})
    @PostMapping("/{productId}/product-stock-history/search")
    public ResponseEntity<Page<ProductStockHistoryResponseDto>> getStockHistoriesByProductIdWithCondition(@RequestBody
    ProductStockHistorySearchConditionDto condition,@PathVariable UUID productId, Pageable pageable) {

        Page<ProductStockHistoryResponseDto> productStockHistoryResponseDtos= productStockHistoryService.getStockHistoriesByProductIdWithCondition(condition,productId,pageable);
        return new ResponseEntity<>(productStockHistoryResponseDtos,HttpStatus.OK);
    }

    @Secured({Authority.MASTER,Authority.HUB_MANAGER})
    @PutMapping("/{productId}/product-stock-history/{productStockHistoryId}")
    public ResponseEntity<ProductStockHistoryResponseDto> updateProductStockHistory(
        @PathVariable UUID productId
        , @PathVariable UUID productStockHistoryId
        , @RequestBody @Valid UpdateStockHistoryRequestDto request) {

        ProductStockHistoryDto productStockHistoryDto = mapperConfig.strictMapper().map(request, ProductStockHistoryDto.class);
        productStockHistoryService.updateProductStockHistory(productStockHistoryDto,productId,productStockHistoryId);
        ProductStockHistoryResponseDto historyInfo = productStockHistoryService.getProductStockHistoryById(productStockHistoryId);
        return new ResponseEntity<>(historyInfo, HttpStatus.OK);
    }

    @Secured({Authority.MASTER,Authority.HUB_MANAGER})
    @DeleteMapping("/{productId}/product-stock-history/{productStockHistoryId}")
    public ResponseEntity<Void> deleteProductStockHistoryById(
        @PathVariable UUID productId
        , @PathVariable UUID productStockHistoryId) {

        productStockHistoryService.deleteProductStockHistory(productId,productStockHistoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
