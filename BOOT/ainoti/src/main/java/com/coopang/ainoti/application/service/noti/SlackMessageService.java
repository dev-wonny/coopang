package com.coopang.ainoti.application.service.noti;


import com.coopang.apidata.application.noti.enums.SlackMessageStatus;
import com.coopang.ainoti.application.request.noti.SlackMessageDto;
import com.coopang.ainoti.application.request.noti.SlackMessageSearchConditionDto;
import com.coopang.ainoti.application.response.noti.SlackMessageResponseDto;
import com.coopang.ainoti.domain.entity.noti.SlackMessageEntity;
import com.coopang.ainoti.domain.repository.noti.SlackMessageRepository;
import com.coopang.ainoti.domain.service.noti.SlackMessageDomainService;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j(topic = "SlackMessageService")
@Transactional
@Service
public class SlackMessageService {

    @Value("${slack.token}")
    private String slackToken;
    private final SlackMessageRepository slackMessageRepository;
    private final SlackMessageDomainService slackMessageDomainService;
    private final Slack slackClient;


    public SlackMessageService(SlackMessageRepository slackMessageRepository, SlackMessageDomainService slackMessageDomainService) {
        this.slackMessageRepository = slackMessageRepository;
        this.slackMessageDomainService = slackMessageDomainService;
        this.slackClient = Slack.getInstance();
    }

    /**
     * Slack 메시지 생성
     */
    @Transactional
    @CacheEvict(value = {"slackMessages", "slackMessageList", "allSlackMessages"}, allEntries = true)
    public SlackMessageResponseDto createSlackMessage(SlackMessageDto slackMessageDto) {
        // UUID 생성
        final UUID slackMessageId = !ObjectUtils.isEmpty(slackMessageDto.getSlackMessageId()) ? slackMessageDto.getSlackMessageId() : UUID.randomUUID();
        slackMessageDto.createId(slackMessageId);

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
    @Cacheable(value = "slackMessageList", key = "#condition.toString()")
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
    @Cacheable(value = "allSlackMessages", key = "#condition.toString() + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<SlackMessageResponseDto> searchSlackMessages(SlackMessageSearchConditionDto condition, Pageable pageable) {
        Page<SlackMessageEntity> slackMessages = slackMessageRepository.search(condition, pageable);
        return slackMessages.map(SlackMessageResponseDto::fromSlackMessage);
    }

    /**
     * Slack 메시지 수정
     */
    @CacheEvict(value = "slackMessages", key = "#slackMessageId")
    public void updateSlackMessage(UUID slackMessageId, SlackMessageDto slackMessageDto) {
        SlackMessageEntity slackMessageEntity = findValidSlackMessageById(slackMessageId);
        slackMessageEntity.updateSlackMessage(
            slackMessageDto.getReceiveSlackId()
            , slackMessageDto.getReceiveUserId()
            , slackMessageDto.getSlackMessageStatus()
            , slackMessageDto.getSlackMessage()
            , slackMessageDto.getSentTime()
            , slackMessageDto.getSlackMessageSenderId()
        );
        log.debug("updateSlackMessage slackMessageId:{}, slackMessageDto:{}", slackMessageId, slackMessageDto.toString());
    }

    /**
     * Slack 메시지 삭제 (논리적 삭제)
     */
    @CacheEvict(value = "slackMessages", key = "#slackMessageId")
    public void deleteSlackMessage(UUID slackMessageId) {
        SlackMessageEntity slackMessageEntity = findValidSlackMessageById(slackMessageId);
        slackMessageEntity.setDeleted(true);
        log.debug("deleteSlackMessage slackMessageId:{}", slackMessageId);
    }

    /**
     * Slack 메시지 전송
     *
     * @param channel Slack 채널 ID 또는 사용자 ID
     * @param message 보낼 메시지 내용
     */
    public void sendSlackMessage(String channel, String message) throws IOException, SlackApiException {
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
            .token(slackToken)//RYqXLjYZnXlX6PRWsRXGDBCA
            .channel(channel) // 채널 ID 또는 사용자 ID
            .text(message)
            .build();

        ChatPostMessageResponse response = slackClient.methods(slackToken).chatPostMessage(request);

        if (!response.isOk()) {
            // Slack API 에러 메시지 처리
            String errorMessage = String.format("Error posting message: %s - %s", response.getError(), response.getErrors());
            String metadataInfo = !ObjectUtils.isEmpty(response.getResponseMetadata()) ? response.getResponseMetadata().toString() : "No metadata available";

            log.error("Slack 메시지 전송 실패: {}. Response Metadata: {}", errorMessage, metadataInfo);

            throw new IOException("Failed to send Slack message. " + errorMessage);
        }

        log.info("Slack 메시지 전송 성공: {}", response.getMessage().getText());
    }

    /**
     * 상태별로 Slack 메시지 조회
     *
     * @param status 메시지 상태
     * @return 상태에 맞는 메시지 리스트
     */
    @Transactional(readOnly = true)
    public List<SlackMessageResponseDto> getMessagesByStatus(SlackMessageStatus status) {
        List<SlackMessageEntity> slackMessageList = slackMessageRepository.findBySlackMessageStatusAndIsDeletedFalse(status);
        return slackMessageList.stream()
            .map(SlackMessageResponseDto::fromSlackMessage)
            .toList();
    }

    /**
     * Slack 메시지 상태 업데이트
     *
     * @param messageId 메시지 ID
     * @param status    새로운 메시지 상태
     */
    @Transactional
    public void updateMessageStatus(UUID messageId, SlackMessageStatus status) {
        SlackMessageEntity messageEntity = findValidSlackMessageById(messageId);
        messageEntity.changeSlackMessageStatus(status);
        log.info("Slack 메시지 상태가 업데이트 되었습니다. 메시지 ID: {}, 새로운 상태: {}", messageId, status);
    }

    public void sendReadySlackMessages() {
        // READY 상태의 메시지 가져오기
        final List<SlackMessageResponseDto> readyMessageList = getMessagesByStatus(SlackMessageStatus.READY);
        final LocalDateTime now = LocalDateTime.now(); // 현재 시간 가져오기

        for (SlackMessageResponseDto message : readyMessageList) {
            if (!ObjectUtils.isEmpty(message.getSentTime()) && now.isAfter(message.getSentTime())) {
                try {
                    // Slack 메시지 전송 시도
                    sendSlackMessage(message.getReceiveSlackId(), message.getSlackMessage());

                    // 성공 시 상태를 SUCCESS로 변경
                    updateMessageStatus(message.getSlackMessageId(), SlackMessageStatus.SUCCESS);
                    log.info("Slack 메시지 전송 성공: {}", message.getSlackMessageId());

                } catch (Exception e) {
                    // 실패 시 상태를 FAIL로 변경
                    updateMessageStatus(message.getSlackMessageId(), SlackMessageStatus.FAIL);
                    log.error("Slack 메시지 전송 실패: {}", message.getSlackMessageId(), e);
                }
            } else {
                log.info("현재 시간에 맞지 않아 전송하지 않음: 메시지 ID: {}, 전송 예정 시간: {}", message.getSlackMessageId(), message.getSentTime());
            }
        }
    }
}
