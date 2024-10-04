package com.coopang.product.presentation.controller;

import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.product.application.request.ProductDto;
import com.coopang.product.application.request.ProductHiddenAndSaleDto;
import com.coopang.product.application.service.ProductService;
import com.coopang.product.presentation.request.BaseSearchCondition;
import com.coopang.product.presentation.request.CreateProductRequestDto;
import com.coopang.product.presentation.request.ProductSearchCondition;
import com.coopang.product.presentation.request.UpdateProductHiddenRequest;
import com.coopang.product.presentation.request.UpdateProductRequest;
import com.coopang.product.presentation.request.UpdateProductSaleRequest;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "ProductController API", description = "ProductController API")
@RestController
@RequestMapping("/products/v1")
@Slf4j(topic = "ProductController")
public class ProductController {

    private final ModelMapperConfig mapperConfig;
    private final ProductService productService;

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";

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

    @Secured({"ROLE_MASTER","ROLE_HUB_MANAGER","ROLE_COMPANY"})
    @PutMapping("/product/{productId}")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody UpdateProductRequest updateProductRequest,@PathVariable UUID productId) {

        ProductDto productDto = mapperConfig.strictMapper().map(updateProductRequest, ProductDto.class);
        productService.updateProduct(productDto,productId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({"ROLE_MASTER","ROLE_HUB_MANAGER","ROLE_COMPANY"})
    @PatchMapping("/product/{productId}/hidden")
    public ResponseEntity<?> updateProductHidden(@Valid @RequestBody UpdateProductHiddenRequest request,@PathVariable UUID productId) {

        ProductHiddenAndSaleDto productHiddenAndSaleDto = mapperConfig.strictMapper().map(request, ProductHiddenAndSaleDto.class);
        productService.updateProductHidden(productHiddenAndSaleDto,productId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({"ROLE_MASTER","ROLE_HUB_MANAGER","ROLE_COMPANY"})
    @PatchMapping("/product/{productId}/sale")
    public ResponseEntity<?> updateProductSale(@Valid @RequestBody UpdateProductSaleRequest request, @PathVariable UUID productId) {

        ProductHiddenAndSaleDto productHiddenAndSaleDto = mapperConfig.strictMapper().map(request, ProductHiddenAndSaleDto.class);
        productService.updateProductSale(productHiddenAndSaleDto,productId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({"ROLE_MASTER","ROLE_HUB_MANAGER","ROLE_COMPANY"})
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID productId,
        @RequestHeader(USER_ID_HEADER) UUID userId,
        @RequestHeader(USER_ROLE_HEADER) String role){

        productService.deleteProductById(userId, role, productId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/product")
    public ResponseEntity<?> getAllProducts(@ModelAttribute BaseSearchCondition condition,
        @RequestHeader(USER_ROLE_HEADER) String role,Pageable pageable) {

        log.info(role);
        return new ResponseEntity<>(productService.getAllProducts(condition,role,pageable),HttpStatus.OK);
    }

    @GetMapping("/product/search")
    public ResponseEntity<?> searchProduct(@ModelAttribute ProductSearchCondition searchCondition,Pageable pageable) {

        return new ResponseEntity<>(productService.searchProduct(searchCondition,pageable),HttpStatus.OK);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable UUID productId) {

        return new ResponseEntity<>(productService.getProductById(productId),HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}/product")
    public ResponseEntity<?> getProductWithCategory(@PathVariable UUID categoryId,Pageable pageable) {

        return new ResponseEntity<>(productService.getProductWithCategory(categoryId,pageable),HttpStatus.OK);
    }
}
