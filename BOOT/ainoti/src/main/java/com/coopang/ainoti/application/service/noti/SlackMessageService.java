package com.coopang.ainoti.application.service.noti;


import com.coopang.ainoti.application.request.noti.SlackMessageDto;
import com.coopang.ainoti.application.request.noti.SlackMessageSearchConditionDto;
import com.coopang.ainoti.application.response.noti.SlackMessageResponseDto;
import com.coopang.ainoti.domain.entity.noti.SlackMessageEntity;
import com.coopang.ainoti.domain.repository.noti.SlackMessageRepository;
import com.coopang.ainoti.domain.service.noti.SlackMessageDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j(topic = "SlackMessageService")
@Transactional
@Service
public class SlackMessageService {

    private final SlackMessageRepository slackMessageRepository;
    private final SlackMessageDomainService slackMessageDomainService;

    public SlackMessageService(SlackMessageRepository slackMessageRepository, SlackMessageDomainService slackMessageDomainService) {
        this.slackMessageRepository = slackMessageRepository;
        this.slackMessageDomainService = slackMessageDomainService;
    }

    /**
     * Slack 메시지 생성
     */
    @Transactional
    public SlackMessageResponseDto createSlackMessage(SlackMessageDto slackMessageDto) {
        // UUID 생성
        final UUID slackMessageId = slackMessageDto.getSlackMessageId() != null ? slackMessageDto.getSlackMessageId() : UUID.randomUUID();
        slackMessageDto.setSlackMessageId(slackMessageId);

        SlackMessageEntity slackMessageEntity = slackMessageDomainService.createSlackMessage(slackMessageDto);
        return SlackMessageResponseDto.fromSlackMessage(slackMessageEntity);
    }

    /**
     * Slack 메시지 조회
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "slackMessages", key = "#slackMessageId")
    public SlackMessageEntity findSlackMessageById(UUID slackMessageId) {
        return slackMessageRepository.findById(slackMessageId)
            .orElseThrow(() -> new IllegalArgumentException("Slack Message not found. slackMessageId=" + slackMessageId));
    }

    /**
     * 유효한 Slack 메시지 조회 (삭제되지 않은 것)
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "slackMessages", key = "#slackMessageId")
    public SlackMessageEntity findValidSlackMessageById(UUID slackMessageId) {
        return slackMessageRepository.findBySlackMessageIdAndIsDeletedFalse(slackMessageId)
            .orElseThrow(() -> new IllegalArgumentException("Slack Message not found. slackMessageId=" + slackMessageId));
    }

    /**
     * 단일 Slack 메시지 조회
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "slackMessages", key = "#slackMessageId")
    public SlackMessageResponseDto getSlackMessageById(UUID slackMessageId) {
        SlackMessageEntity slackMessageEntity = findSlackMessageById(slackMessageId);
        return SlackMessageResponseDto.fromSlackMessage(slackMessageEntity);
    }

    /**
     * 유효한 Slack 메시지 조회
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "slackMessages", key = "#slackMessageId")
    public SlackMessageResponseDto getValidSlackMessageById(UUID slackMessageId) {
        SlackMessageEntity slackMessageEntity = findValidSlackMessageById(slackMessageId);
        return SlackMessageResponseDto.fromSlackMessage(slackMessageEntity);
    }

    /**
     * Slack 메시지 리스트 조회
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "slackMessageList", key = "#condition")
    public List<SlackMessageResponseDto> getSlackMessageList(SlackMessageSearchConditionDto condition) {
        List<SlackMessageEntity> slackMessages = slackMessageRepository.findSlackMessageList(condition);
        return slackMessages.stream()
            .map(SlackMessageResponseDto::fromSlackMessage)
            .toList();
    }

    /**
     * Slack 메시지 검색 (페이징, 정렬, 키워드 검색)
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "allSlackMessages", key = "#condition")
    public Page<SlackMessageResponseDto> searchSlackMessages(SlackMessageSearchConditionDto condition, Pageable pageable) {
        Page<SlackMessageEntity> slackMessages = slackMessageRepository.search(condition, pageable);
        return slackMessages.map(SlackMessageResponseDto::fromSlackMessage);
    }

    /**
     * Slack 메시지 수정
     */
    @CacheEvict(value = "slackMessages", key = "#slackMessageId")
    public void updateSlackMessage(UUID slackMessageId, SlackMessageDto slackMessageDto) {
        SlackMessageEntity slackMessageEntity = findSlackMessageById(slackMessageId);
        slackMessageEntity.updateSlackMessage(slackMessageDto.getSlackMessage(), slackMessageDto.getSlackMessageStatus(), slackMessageDto.getSentTime());
        log.debug("updateSlackMessage slackMessageId:{}", slackMessageId);
    }

    /**
     * Slack 메시지 삭제 (논리적 삭제)
     */
    @CacheEvict(value = "slackMessages", key = "#slackMessageId")
    public void deleteSlackMessage(UUID slackMessageId) {
        SlackMessageEntity slackMessageEntity = findSlackMessageById(slackMessageId);
        slackMessageEntity.setDeleted(true);
        log.debug("deleteSlackMessage slackMessageId:{}", slackMessageId);
    }
}
