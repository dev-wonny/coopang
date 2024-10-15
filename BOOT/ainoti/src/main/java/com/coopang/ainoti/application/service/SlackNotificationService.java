package com.coopang.ainoti.application.service;

import com.coopang.ainoti.application.request.SlackMessageDto;
import com.coopang.ainoti.application.response.SlackNotificationResponseDto;
import com.coopang.ainoti.domain.entitiy.slacknotification.SlackNotificationEntity;
import com.coopang.ainoti.domain.entitiy.slacknotification.SlackNotificationStatus;
import com.coopang.ainoti.domain.repository.slacknotification.SlackNotificationRepository;
import com.coopang.ainoti.domain.service.SlackNotificationDomainService;
import com.coopang.ainoti.presentation.request.SlackMessageSearchConditionDto;
import com.coopang.ainoti.presentation.request.UpdateSlackNotificationRequestDto;
import com.coopang.apicommunication.feignClient.user.UserClient;
import com.coopang.apicommunication.kafka.message.LowStockNotification;
import com.coopang.apidata.application.user.response.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackNotificationService {

    private final SlackNotificationRepository slackNotificationRepository;
    private final SlackNotificationDomainService slackNotificationDomainService;
    private final WebClient webClient;
    private final UserClient userClient;

    //슬랙 메시지 생성
    public SlackNotificationResponseDto createMessage(SlackMessageDto dto) {

        return SlackNotificationResponseDto.of(slackNotificationDomainService.createMessage(dto));
    }

    //슬랙 메시지 논리적 삭제
    @Transactional
    public void deleteMessage(UUID slackMessageId) {

        SlackNotificationEntity slackNotificationEntity = findBySlackMessageId(slackMessageId);

        slackNotificationEntity.setDeleted(true);
    }

    //슬랙 메시지 업데이트
    @Transactional
    public void updateMessage(UpdateSlackNotificationRequestDto updateSlackNotificationRequestDto, UUID slackMessageId) {
        SlackNotificationEntity slackNotificationEntity = findBySlackMessageId(slackMessageId);

        slackNotificationEntity.update(
            updateSlackNotificationRequestDto.getReceiveSlackId(),
            updateSlackNotificationRequestDto.getReceiveUserId(),
            updateSlackNotificationRequestDto.getSlackNotificationStatus(),
            updateSlackNotificationRequestDto.getSlackMessage(),
            updateSlackNotificationRequestDto.getSlackMessageSenderId()
        );
    }

    //슬랙 메시지 조회하는 공통함수
    private SlackNotificationEntity findBySlackMessageId(UUID slackMessageId) {
        return slackNotificationRepository.findBySlackMessageIdAndIsDeletedFalse(slackMessageId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 슬랙메시지 아이디입니다.")
        );
    }

    //슬렉 메시지 단일조회
    @Transactional(readOnly = true)
    public SlackNotificationResponseDto getSlackMessage(UUID slackMessageId) {

        return SlackNotificationResponseDto.of(findBySlackMessageId(slackMessageId));
    }

    //슬랙 메시지 목록 조회 - 페이징 지원
    public Page<SlackNotificationResponseDto> getSlackMessagesWithPageable(Pageable pageable) {

        return slackNotificationRepository.search(pageable).map(SlackNotificationResponseDto::of);
    }

    //슬랙 메시지 목록 조회 - 페이징 지원,키워드 검색
    public Page<SlackNotificationResponseDto> searchWithCondition(SlackMessageSearchConditionDto conditionDto, Pageable pageable) {
        return slackNotificationRepository.searchWithCondition(conditionDto, pageable).map(
            SlackNotificationResponseDto::of);
    }

    //재고 부족 알림 전송
    public void listenerSendSlackMessage(LowStockNotification lowStockNotification){

        try {
            //관리자의 user info 조회
            UserResponse userResponse = userClient.getUserInfo(lowStockNotification.getManagerId());
            String slackId = userResponse.getSlackId();
            String message = lowStockNotification.getProductId() + "의 재고가 부족합니다. 현재 수량은 "+ lowStockNotification.getQuantity() +"입니다.";
            //개인 채널 값 가져오기
            String channelId = makeDmChannel(slackId);

            //DM 보낼 값 생성
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("channel", channelId);
            requestBody.put("text", message);

            String response = webClient.post()
                .uri("api/chat.postMessage")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();  // 비동기 처리에서는 block() 대신 다른 방식 사용

            log.info(response);

            // JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            // 요청이 성공했는지 확인
            isOk(jsonNode);

            SlackMessageDto slackMessageDto = new SlackMessageDto();
            slackMessageDto.setSlackMessage(message);
            slackMessageDto.setReceiveSlackId(slackId);
            slackMessageDto.setSlackNotificationStatus(SlackNotificationStatus.SUCCESS);
            slackMessageDto.setReceiveUserId(userResponse.getUserId());
            slackMessageDto.setSlackMessageSenderId("test senderId");

            slackNotificationDomainService.createMessage(slackMessageDto);

        }catch (JsonProcessingException e){
            log.error(e.getMessage());
        }

    }

    //슬랙 메시지 전송
    public void sendSlackMessage(String slackId,String message){

        try {
            //1. 개인 채널 값 가져오기
            String channelId = makeDmChannel(slackId);

            //2. DM 보낼 값 생성
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("channel", channelId);
            requestBody.put("text", message);

            String response = webClient.post()
                .uri("api/chat.postMessage")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();  // 비동기 처리에서는 block() 대신 다른 방식 사용

            log.info(response);

            // JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            // 요청이 성공했는지 확인
            isOk(jsonNode);

        }catch (JsonProcessingException e){
            log.error(e.getMessage());
        }
    }

    //해당 슬랙 아이디의 DM을 보내기전 채널 값 조회
    private String makeDmChannel(String slackId) throws JsonProcessingException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("users", slackId);

        String response = webClient.post()
            .uri("/api/conversations.open")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String.class)
            .block();  // 비동기 처리에서는 block() 대신 다른 방식 사용

        log.info("slack Resposne " + response);
        // JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        // 요청이 성공했는지 확인
        isOk(jsonNode);

        // "channel"의 "id" 값을 가져오기
        String channelId = jsonNode.path("channel").path("id").asText();

        log.info("Slack Channel ID: " + channelId);

        return channelId;
    }

    //슬랙 API 응답 결과
    private static void isOk(JsonNode jsonNode) {
        boolean isOk = jsonNode.path("ok").asBoolean();
        if (!isOk) {
            String error = jsonNode.path("error").asText();
            log.error("Slack API Error: " + error);
            throw new RuntimeException("Failed to open DM channel: " + error);
        }
    }

}
