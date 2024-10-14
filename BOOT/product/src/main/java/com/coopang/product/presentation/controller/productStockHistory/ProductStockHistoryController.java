package com.coopang.product.presentation.controller.productStockHistory;

import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.apidata.application.user.enums.UserRoleEnum.Authority;
import com.coopang.product.application.request.ProductStockHistory.ProductStockHistoryDto;
import com.coopang.product.application.service.productStockHistory.ProductStockHistoryService;
import com.coopang.product.presentation.request.productStockHistory.ProductStockHistorySearchConditionDto;
import com.coopang.product.presentation.request.productStockHistory.UpdateStockHistoryRequestDto;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<?> getStockHistoriesByProductId(@PathVariable UUID productId, Pageable pageable) {

        return new ResponseEntity<>(
            productStockHistoryService.getStockHistoriesByProductId(productId,pageable),HttpStatus.OK);
    }

    @Secured({Authority.MASTER,Authority.COMPANY,Authority.HUB_MANAGER})
    @GetMapping("/{productId}/product-stock-history/search")
    public ResponseEntity<?> getStockHistoriesByProductIdWithCondition(@ModelAttribute
    ProductStockHistorySearchConditionDto condition,@PathVariable UUID productId, Pageable pageable) {

        return new ResponseEntity<>(
            productStockHistoryService.getStockHistoriesByProductIdWithCondition(condition,productId,pageable),HttpStatus.OK);
    }

    @Secured({Authority.MASTER,Authority.HUB_MANAGER})
    @PutMapping("/{productId}/product-stock-history/{productStockHistoryId}")
    public ResponseEntity<?> updateProductStockHistory(@PathVariable UUID productId, @PathVariable UUID productStockHistoryId,
        @RequestBody @Valid UpdateStockHistoryRequestDto request) {

        ProductStockHistoryDto productStockHistoryDto = mapperConfig.strictMapper().map(request, ProductStockHistoryDto.class);
        productStockHistoryService.updateProductStockHistory(productStockHistoryDto,productId,productStockHistoryId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({Authority.MASTER,Authority.HUB_MANAGER})
    @DeleteMapping("/{productId}/product-stock-history/{productStockHistoryId}")
    public ResponseEntity<?> deleteProductStockHistoryById(@PathVariable UUID productId, @PathVariable UUID productStockHistoryId) {

        productStockHistoryService.deleteProductStockHistory(productId,productStockHistoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
