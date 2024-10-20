package com.coopang.ainoti.presentation.controller.ai;


import com.coopang.ainoti.application.request.ai.AiRequestHistoryDto;
import com.coopang.ainoti.application.request.ai.AiRequestHistorySearchConditionDto;
import com.coopang.ainoti.application.response.ai.AiRequestHistoryResponseDto;
import com.coopang.ainoti.application.service.ai.AiRequestHistoryService;
import com.coopang.ainoti.presentation.request.ai.AiRequestHistorySearchConditionRequestDto;
import com.coopang.ainoti.presentation.request.ai.CreateAiRequestHistoryRequestDto;
import com.coopang.ainoti.presentation.request.ai.UpdateAiRequestHistoryRequestDto;
import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.coredata.user.enums.UserRoleEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "AiRequestHistoryController API", description = "AI 요청 기록 관리 API")
@Slf4j(topic = "AiRequestHistoryController")
@RestController
@RequestMapping("/ai-request-histories/v1/ai-request-history")
public class AiRequestHistoryController {

    private final AiRequestHistoryService aiRequestHistoryService;
    private final ModelMapperConfig mapperConfig;

    public AiRequestHistoryController(AiRequestHistoryService aiRequestHistoryService, ModelMapperConfig mapperConfig) {
        this.aiRequestHistoryService = aiRequestHistoryService;
        this.mapperConfig = mapperConfig;
    }

    /**
     * AI 요청 기록 생성
     *
     * @param req
     * @return
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @PostMapping
    public ResponseEntity<AiRequestHistoryResponseDto> createAiRequestHistory(@Valid @RequestBody CreateAiRequestHistoryRequestDto req) {
        final AiRequestHistoryDto aiRequestHistoryDto = mapperConfig.strictMapper().map(req, AiRequestHistoryDto.class);
        final AiRequestHistoryResponseDto response = aiRequestHistoryService.createAiRequestHistory(aiRequestHistoryDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 단일 AI 요청 기록 조회
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @GetMapping("/{aiRequestHistoryId}")
    public ResponseEntity<AiRequestHistoryResponseDto> getAiRequestHistoryById(@PathVariable UUID aiRequestHistoryId) {
        AiRequestHistoryResponseDto aiRequestHistory = aiRequestHistoryService.getValidAiRequestHistoryById(aiRequestHistoryId);
        return new ResponseEntity<>(aiRequestHistory, HttpStatus.OK);
    }

    /**
     * AI 요청 기록 리스트 조회
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @PostMapping("/list")
    public ResponseEntity<List<AiRequestHistoryResponseDto>> getAiRequestHistoryList(@RequestBody AiRequestHistorySearchConditionRequestDto req) {
        final AiRequestHistorySearchConditionDto condition = mapperConfig.strictMapper().map(req, AiRequestHistorySearchConditionDto.class);
        final List<AiRequestHistoryResponseDto> responseList = aiRequestHistoryService.getAiRequestHistoryList(condition);
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    /**
     * AI 요청 기록 검색 (페이징, 정렬, 키워드 검색)
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @PostMapping("/search")
    public ResponseEntity<Page<AiRequestHistoryResponseDto>> searchAiRequestHistories(@RequestBody AiRequestHistorySearchConditionRequestDto req, Pageable pageable) {
        final AiRequestHistorySearchConditionDto condition = mapperConfig.strictMapper().map(req, AiRequestHistorySearchConditionDto.class);
        Page<AiRequestHistoryResponseDto> responsePage = aiRequestHistoryService.searchAiRequestHistories(condition, pageable);
        return new ResponseEntity<>(responsePage, HttpStatus.OK);
    }

    /**
     * AI 요청 기록 수정
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @PutMapping("/{aiRequestHistoryId}")
    public ResponseEntity<AiRequestHistoryResponseDto> updateAiRequestHistory(@PathVariable UUID aiRequestHistoryId, @Valid @RequestBody UpdateAiRequestHistoryRequestDto req) {
        final AiRequestHistoryDto aiRequestHistoryDto = mapperConfig.strictMapper().map(req, AiRequestHistoryDto.class);
        aiRequestHistoryService.updateAiRequestHistory(aiRequestHistoryId, aiRequestHistoryDto);
        final AiRequestHistoryResponseDto aiRequestHistory = aiRequestHistoryService.getAiRequestHistoryById(aiRequestHistoryId);
        return new ResponseEntity<>(aiRequestHistory, HttpStatus.OK);
    }

    /**
     * 단일 AI 요청 기록 삭제 (논리적 삭제)
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @DeleteMapping("/{aiRequestHistoryId}")
    public ResponseEntity<Void> deleteAiRequestHistory(@PathVariable UUID aiRequestHistoryId) {
        aiRequestHistoryService.deleteAiRequestHistory(aiRequestHistoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
