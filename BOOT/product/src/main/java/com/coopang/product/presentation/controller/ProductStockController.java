package com.coopang.product.presentation.controller;

import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product-stocks/v1")
@RequiredArgsConstructor
public class ProductStockController {

    //private final ProductStockService productStockService;

    @PatchMapping("/product/{productId}/productStockId/{productStockId}")
    public ResponseEntity<String> updateProductStock(
        @PathVariable UUID productId,
        @PathVariable UUID productStockId,
        @RequestBody Map<String, Object> stockUpdateRequest) {
        // 상품 재고 수량 변경 로직
        String operation = (String) stockUpdateRequest.get("operation");
        Integer amount = (Integer) stockUpdateRequest.get("amount");

        if (operation == null || amount == null || amount <= 0) {
            return ResponseEntity.badRequest().body("Invalid request data");
        }

        //productStockService.updateStock(productId, productStockId, operation, amount);
        return ResponseEntity.ok("Stock updated successfully");
    }
}
