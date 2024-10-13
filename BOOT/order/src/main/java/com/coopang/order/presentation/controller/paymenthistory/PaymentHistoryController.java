package com.coopang.order.presentation.controller.paymenthistory;

import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.order.application.request.paymenthistory.PaymentHistoryDto;
import com.coopang.order.application.response.paymenthistory.PaymentHistoryResponseDto;
import com.coopang.order.application.service.paymenthistory.PaymentHistoryService;
import com.coopang.order.presentation.request.paymenthistory.PaymentHistoryRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "PaymentHistoryController API", description = "PaymentHistoryController API")
@Slf4j(topic = "PaymentHistoryController")
@RestController
@RequestMapping("/payment-histories/v1")
public class PaymentHistoryController {

    private final ModelMapperConfig mapperConfig;

    public PaymentHistoryController(ModelMapperConfig mapperConfig) {
        this.mapperConfig = mapperConfig;
    }

    /*
    Todo : 결제기록 생성
     */
    @PostMapping("/payment-history")
    @Secured({UserRoleEnum.Authority.MASTER,UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<PaymentHistoryResponseDto> createPaymentHistory(@RequestBody PaymentHistoryRequestDto paymentHistoryRequestDto) {
        PaymentHistoryDto paymentHistoryDto = mapperConfig.standardMapper().map(paymentHistoryRequestDto, PaymentHistoryDto.class);
        PaymentHistoryResponseDto paymentHistoryResponseDto = paymentHistoryService.createPaymentHistory(paymentHistoryDto);
        return new ResponseEntity<>(paymentHistoryResponseDto, HttpStatus.CREATED);
    }

    /*
    Todo : 결제기록 단건 조회
     */
    @GetMapping("/payment-history/{paymentHistoryId}")
    @Secured({UserRoleEnum.Authority.MASTER,UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<PaymentHistoryResponseDto> getPaymentHistory(@PathVariable UUID paymentHistoryId) {
        PaymentHistoryResponseDto paymentHistoryResponseDto = paymentHistoryService.findById(paymentHistoryId);
        return new ResponseEntity<>(paymentHistoryResponseDto, HttpStatus.OK);
    }

    /*
    Todo : 결제기록 전체 조회
     */
    @GetMapping("/payment-history")
    @Secured({UserRoleEnum.Authority.MASTER,UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<Page<PaymentHistoryResponseDto>> getAllPaymentHistory(
            Pageable pageable
    ){
        Page<PaymentHistoryResponseDto> paymentHistoryResponseDtos = paymentHistoryService.findAllPaymentHistory(pageable);
        return new ResponseEntity<>(paymentHistoryResponseDtos, HttpStatus.OK);
    }


    /*
    Todo : 결제기록 검색
     */
    @GetMapping("/payment-history/search")
    @Secured({UserRoleEnum.Authority.MASTER,UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<Page<PaymentHistoryResponseDto>> getAllSearchPaymentHistory(
            Pageable pageable
    ){
        Page<PaymentHistoryResponseDto> paymentHistoryResponseDtos = paymentHistoryService.findAllSearchPaymentHistory(pageable);
        return new ResponseEntity<>(paymentHistoryResponseDtos, HttpStatus.OK);
    }

    /*
    Todo : 결제기록 수정
     */
    @PutMapping("/payment-history/{paymentHistoryId}")
    @Secured({UserRoleEnum.Authority.MASTER,UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<PaymentHistoryResponseDto> updatePaymentHistory(
            @PathVariable UUID paymentHistoryId
    ) {
        paymentHistoryService.updatePaymentHistory(paymentHistoryId);
        final PaymentHistoryResponseDto paymentHistoryInfo = paymentHistoryService.findById(paymentHistoryId);
        return new ResponseEntity<>(paymentHistoryInfo, HttpStatus.OK);
    }

    /*
    Todo : 결제기록 삭제
     */
    @DeleteMapping("/payment-history/{paymentHistoryId}")
    @Secured({UserRoleEnum.Authority.MASTER,UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<Void> deletePaymentHistory(
            @PathVariable UUID paymentHistoryId
    ){
        paymentHistoryService.deletePaymentHistory(paymentHistoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
