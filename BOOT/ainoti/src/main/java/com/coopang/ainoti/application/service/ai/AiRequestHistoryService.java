package com.coopang.ainoti.application.service.ai;


import com.coopang.ainoti.application.request.ai.AiRequestHistoryDto;
import com.coopang.ainoti.application.request.ai.AiRequestHistorySearchConditionDto;
import com.coopang.ainoti.application.response.ai.AiRequestHistoryResponseDto;
import com.coopang.ainoti.domain.entity.ai.AiRequestHistoryEntity;
import com.coopang.ainoti.domain.repository.ai.AiRequestHistoryRepository;
import com.coopang.ainoti.domain.service.ai.AiRequestHistoryDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j(topic = "AiRequestHistoryService")
@Transactional
@Service
public class AiRequestHistoryService {

    private final AiRequestHistoryRepository aiRequestHistoryRepository;
    private final AiRequestHistoryDomainService aiRequestHistoryDomainService;

    public AiRequestHistoryService(AiRequestHistoryRepository aiRequestHistoryRepository, AiRequestHistoryDomainService aiRequestHistoryDomainService) {
        this.aiRequestHistoryRepository = aiRequestHistoryRepository;
        this.aiRequestHistoryDomainService = aiRequestHistoryDomainService;
    }

    /**
     * AI 요청 기록 생성
     */
    @Transactional
    public AiRequestHistoryResponseDto createAiRequestHistory(AiRequestHistoryDto aiRequestHistoryDto) {
        // UUID 생성
        final UUID aiRequestHistoryId = aiRequestHistoryDto.getAiRequestHistoryId() != null ? aiRequestHistoryDto.getAiRequestHistoryId() : UUID.randomUUID();
        aiRequestHistoryDto.setAiRequestHistoryId(aiRequestHistoryId);

        AiRequestHistoryEntity aiRequestHistoryEntity = aiRequestHistoryDomainService.createAiRequestHistory(aiRequestHistoryDto);
        return AiRequestHistoryResponseDto.fromAiRequestHistory(aiRequestHistoryEntity);
    }

    /**
     * AI 요청 기록 조회
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "aiRequestHistories", key = "#aiRequestHistoryId")
    public AiRequestHistoryEntity findAiRequestHistoryById(UUID aiRequestHistoryId) {
        return aiRequestHistoryRepository.findById(aiRequestHistoryId)
            .orElseThrow(() -> new IllegalArgumentException("AI Request History not found. aiRequestHistoryId=" + aiRequestHistoryId));
    }

    /**
     * 유효한 AI 요청 기록 조회 (삭제되지 않은 것)
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "aiRequestHistories", key = "#aiRequestHistoryId")
    public AiRequestHistoryEntity findValidAiRequestHistoryById(UUID aiRequestHistoryId) {
        return aiRequestHistoryRepository.findByAiRequestHistoryIdAndIsDeletedFalse(aiRequestHistoryId)
            .orElseThrow(() -> new IllegalArgumentException("AI Request History not found. aiRequestHistoryId=" + aiRequestHistoryId));
    }

    /**
     * 단일 AI 요청 기록 조회
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "aiRequestHistories", key = "#aiRequestHistoryId")
    public AiRequestHistoryResponseDto getAiRequestHistoryById(UUID aiRequestHistoryId) {
        AiRequestHistoryEntity aiRequestHistoryEntity = findValidAiRequestHistoryById(aiRequestHistoryId);
        return AiRequestHistoryResponseDto.fromAiRequestHistory(aiRequestHistoryEntity);
    }

    /**
     * 유효한 AI 요청 기록 조회
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "aiRequestHistories", key = "#aiRequestHistoryId")
    public AiRequestHistoryResponseDto getValidAiRequestHistoryById(UUID aiRequestHistoryId) {
        AiRequestHistoryEntity aiRequestHistoryEntity = findValidAiRequestHistoryById(aiRequestHistoryId);
        return AiRequestHistoryResponseDto.fromAiRequestHistory(aiRequestHistoryEntity);
    }

    /**
     * AI 요청 기록 리스트 조회
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "aiRequestHistoryList", key = "#condition")
    public List<AiRequestHistoryResponseDto> getAiRequestHistoryList(AiRequestHistorySearchConditionDto condition) {
        List<AiRequestHistoryEntity> aiRequestHistories = aiRequestHistoryRepository.findAiRequestHistoryList(condition);
        return aiRequestHistories.stream()
            .map(AiRequestHistoryResponseDto::fromAiRequestHistory)
            .toList();
    }

    /**
     * AI 요청 기록 검색 (페이징, 정렬, 키워드 검색)
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "allAiRequestHistories", key = "#condition")
    public Page<AiRequestHistoryResponseDto> searchAiRequestHistories(AiRequestHistorySearchConditionDto condition, Pageable pageable) {
        Page<AiRequestHistoryEntity> aiRequestHistories = aiRequestHistoryRepository.search(condition, pageable);
        return aiRequestHistories.map(AiRequestHistoryResponseDto::fromAiRequestHistory);
    }

    /**
     * AI 요청 기록 수정
     */
    @CacheEvict(value = "aiRequestHistories", key = "#aiRequestHistoryId")
    public void updateAiRequestHistory(UUID aiRequestHistoryId, AiRequestHistoryDto aiRequestHistoryDto) {
        AiRequestHistoryEntity aiRequestHistoryEntity = findAiRequestHistoryById(aiRequestHistoryId);
        aiRequestHistoryEntity.updateAiRequestResponse(aiRequestHistoryDto.getAiRequest(), aiRequestHistoryDto.getAiResponse());
        log.debug("updateAiRequestHistory aiRequestHistoryId:{}", aiRequestHistoryId);
    }

    /**
     * AI 요청 기록 삭제 (논리적 삭제)
     */
    @CacheEvict(value = "aiRequestHistories", key = "#aiRequestHistoryId")
    public void deleteAiRequestHistory(UUID aiRequestHistoryId) {
        AiRequestHistoryEntity aiRequestHistoryEntity = findValidAiRequestHistoryById(aiRequestHistoryId);
        aiRequestHistoryEntity.setDeleted(true);
        log.debug("deleteAiRequestHistory aiRequestHistoryId:{}", aiRequestHistoryId);
    }
}
