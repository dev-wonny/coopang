package com.coopang.product.domain.utill;

import com.coopang.product.domain.entitiy.CategoryEntity;
import com.coopang.product.infrastructure.repository.CategoryJpaRepository;
import java.util.Arrays;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class TestCategoryDataRunner implements ApplicationRunner {

    private final CategoryJpaRepository categoryRepository;

    TestCategoryDataRunner(CategoryJpaRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        List<CategoryEntity> categories = Arrays.asList(
            new CategoryEntity("식품"),
            new CategoryEntity("음료"),
            new CategoryEntity("식자재"),
            new CategoryEntity("가공식품"),
            new CategoryEntity("의류"),
            new CategoryEntity("패션잡화"),
            new CategoryEntity("신발"),
            new CategoryEntity("생활용품"),
            new CategoryEntity("가전제품"),
            new CategoryEntity("디지털 기기"),
            new CategoryEntity("화장품/뷰티"),
            new CategoryEntity("유아용품"),
            new CategoryEntity("건강/의료"),
            new CategoryEntity("스포츠"),
            new CategoryEntity("가구"),
            new CategoryEntity("자동차용품"),
            new CategoryEntity("펫용품"),
            new CategoryEntity("취미/완구"),
            new CategoryEntity("공구"),
            new CategoryEntity("도서")
        );

        // 리포지토리에 데이터 저장
        categoryRepository.saveAll(categories);
    }

}
