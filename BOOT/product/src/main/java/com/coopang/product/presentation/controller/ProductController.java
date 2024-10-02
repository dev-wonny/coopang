package com.coopang.product.presentation.controller;

import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.product.application.request.ProductDto;
import com.coopang.product.application.service.ProductService;
import com.coopang.product.presentation.request.CreateProductRequestDto;
import com.coopang.product.presentation.request.ProductSearchCondition;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "ProductController API", description = "ProductController API")
@RestController
@RequestMapping("/products/v1")
@Slf4j(topic = "ProductController")
public class ProductController {

    private final ModelMapperConfig mapperConfig;
    private final ProductService productService;

    public ProductController(ModelMapperConfig mapperConfig,ProductService productService) {
        this.mapperConfig = mapperConfig;
        this.productService = productService;
    }

    @Secured({"ROLE_MASTER","ROLE_HUB_MANAGER","ROLE_COMPANY"})
    @PostMapping("/product")
    public ResponseEntity<?> createProduct(@Valid @RequestBody CreateProductRequestDto createProductRequestDto) {
        ProductDto productDto = mapperConfig.strictMapper().map(createProductRequestDto, ProductDto.class);

        return new ResponseEntity<>(productService.createProduct(productDto),HttpStatus.CREATED);
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable UUID productId) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/product/{productId}/hidden")
    public ResponseEntity<?> updateHiddenProduct(@PathVariable UUID productId) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/product/{productId}/sale")
    public ResponseEntity<?> updateSaleProduct(@PathVariable UUID productId) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/product/{productId}/sale")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID productId) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/product")
    public ResponseEntity<?> getAllProducts(@ModelAttribute ProductSearchCondition searchCondition,
        Pageable pageable) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/product/search")
    public ResponseEntity<?> searchProduct(@ModelAttribute ProductSearchCondition searchCondition,Pageable pageable) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable UUID productId) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}/product")
    public ResponseEntity<?> getProductWithCategory(@PathVariable UUID categoryId,Pageable pageable) {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
