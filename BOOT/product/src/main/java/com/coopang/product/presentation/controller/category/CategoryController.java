package com.coopang.product.presentation.controller.category;

import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.coredata.user.enums.UserRoleEnum.Authority;
import com.coopang.product.application.request.category.CategoryDto;
import com.coopang.product.application.response.category.CategoryResponseDto;
import com.coopang.product.application.service.category.CategoryService;
import com.coopang.product.presentation.request.category.CreateCategoryRequestDto;
import com.coopang.product.presentation.request.category.UpdateCategoryRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CategoryController API", description = "CategoryController API")
@RestController
@RequestMapping("/categories/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapperConfig mapperConfig;

    @Secured({Authority.MASTER, Authority.HUB_MANAGER})
    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(
        @Valid @RequestBody CreateCategoryRequestDto requestDto) {

        CategoryDto categoryDto = mapperConfig.strictMapper().map(requestDto, CategoryDto.class);
        CategoryResponseDto categoryResponseDto = categoryService.createCategory(categoryDto);

        return new ResponseEntity<>(categoryResponseDto,HttpStatus.CREATED);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getOneCategory(@PathVariable UUID categoryId) {

        CategoryResponseDto categoryResponseDto = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(categoryResponseDto,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategory() {

        List<CategoryResponseDto> categoryResponseDtoList = categoryService.getAllCategory();
        return new ResponseEntity<>(categoryResponseDtoList,HttpStatus.OK);
    }

    @Secured({Authority.MASTER, Authority.HUB_MANAGER})
    @PatchMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
        @PathVariable UUID categoryId
        , @Valid @RequestBody UpdateCategoryRequestDto updateCategoryRequestDto)
    {
        CategoryDto categoryDto = mapperConfig.strictMapper().map(updateCategoryRequestDto,CategoryDto.class);
        categoryService.updateCategory(categoryId,categoryDto);
        CategoryResponseDto categoryResponseDto = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(categoryResponseDto,HttpStatus.OK);
    }
    @Secured({Authority.MASTER, Authority.HUB_MANAGER})
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
