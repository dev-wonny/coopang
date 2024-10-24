package com.coopang.product.presentation.controller.product;

import com.coopang.product.application.service.message.product.ProductMessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/productMessages/v1")
public class ProductMessageController {

    private final ProductMessageService productMessageService;

    public ProductMessageController(ProductMessageService productMessageService) {
        this.productMessageService = productMessageService;
    }

    @GetMapping
    public void sendLowStockNotification(@RequestParam(value = "productId") UUID productId, @RequestParam(value = "currentQuantity") int currentQuantity) {
        productMessageService.sendLowStockNotification(productId, currentQuantity);
    }
}
