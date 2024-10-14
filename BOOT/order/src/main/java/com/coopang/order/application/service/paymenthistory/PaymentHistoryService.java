package com.coopang.order.application.service.paymenthistory;

import com.coopang.order.application.request.paymenthistory.PaymentHistoryDto;
import com.coopang.order.application.response.paymenthistory.PaymentHistoryResponseDto;
import com.coopang.order.domain.entity.paymenthistory.PaymentHistoryEntity;
import com.coopang.order.domain.repository.paymenthistory.PaymentHistoryRepository;
import com.coopang.order.domain.service.payment.PaymentHistoryDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j(topic = "PaymentService")
@Service
@Transactional
public class PaymentHistoryService {

    private final PaymentHistoryRepository paymentHistoryRepository;
    private final PaymentHistoryDomainService paymentHistoryDomainService;

    public PaymentHistoryService(
            PaymentHistoryRepository paymentHistoryRepository,
            PaymentHistoryDomainService paymentHistoryDomainService
    ) {
        this.paymentHistoryRepository = paymentHistoryRepository;
        this.paymentHistoryDomainService = paymentHistoryDomainService;
    }

    /*
    Todo : 결제 기록 생성
     */
    public PaymentHistoryResponseDto createPaymentHistory(PaymentHistoryDto paymentHistoryDto) {
        PaymentHistoryEntity paymentHistoryEntity = paymentHistoryDomainService.createPaymentHistory(paymentHistoryDto);
        log.info("PaymentHistory created successfully: {}", paymentHistoryEntity);
        return PaymentHistoryResponseDto.fromPaymentHistory(paymentHistoryEntity);
    }
    /*
    Todo : 결제 기록 단건 조회
     */
    @Transactional(readOnly = true)
    public PaymentHistoryResponseDto findById(UUID paymentHistoryId) {
        PaymentHistoryEntity paymentHistoryEntity = findByPaymentHistoryId(paymentHistoryId);
        return PaymentHistoryResponseDto.fromPaymentHistory(paymentHistoryEntity);
    }
    /*
    Todo : 결제 기록 전체 조회
     */
//    @Transactional(readOnly = true)
//    public Page<PaymentHistoryResponseDto> findAllPaymentHistory(Pageable pageable) {
//        Page<PaymentHistoryEntity> paymentHistoryEntities = paymentHistoryRepository.findAllbyPaymentHistory(pageable);
//        return paymentHistoryEntities.map(PaymentHistoryResponseDto::fromPaymentHistory);
//    }

    /*
    Todo : 결제 기록 검색
     */
//    @Transactional(readOnly = true)
//    public Page<PaymentHistoryResponseDto> findAllSearchPaymentHistory(Pageable pageable) {
//        Page<PaymentHistoryEntity> paymentHistoryEntities = paymentHistoryRepository.findAllbyPaymentHistorySearch(pageable);
//        return paymentHistoryEntities.map(PaymentHistoryResponseDto::fromPaymentHistory);
//    }

    /*
    Todo : 결제 기록 수정
     */
    public void updatePaymentHistory(UUID paymentHistoryId){
        PaymentHistoryEntity paymentHistoryEntity = findByPaymentHistoryId(paymentHistoryId);
        log.info("PaymentHistory update successfully: {}", paymentHistoryEntity);
    }

    /*
    Todo : 결제 기록 삭제
     */
    public void deletePaymentHistory(UUID paymentHistoryId){
        PaymentHistoryEntity paymentHistoryEntity = findByPaymentHistoryId(paymentHistoryId);
        paymentHistoryEntity.setDeleted(true);
        log.debug("deletePaymentHistory successfully: {}", paymentHistoryEntity);
    }
    /*
    공통 메서드
    1. id 값 찾기
     */
    private PaymentHistoryEntity findByPaymentHistoryId(UUID paymentHistoryId){
        return paymentHistoryRepository.findById(paymentHistoryId)
                .orElseThrow(() -> new IllegalArgumentException("PaymentHistory not found. paymentHistoryId=" + paymentHistoryId));
    }

}
