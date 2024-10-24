package com.coopang.ainoti.presentation.controller.ai;


import com.coopang.ainoti.application.request.ai.AiRequestHistoryDto;
import com.coopang.ainoti.application.request.ai.AiRequestHistorySearchConditionDto;
import com.coopang.ainoti.application.response.ai.AiRequestHistoryResponseDto;
import com.coopang.ainoti.application.service.ai.AiRequestHistoryService;
import com.coopang.ainoti.presentation.request.ai.AiRequestHistorySearchConditionRequestDto;
import com.coopang.ainoti.presentation.request.ai.CreateAiRequestHistoryRequestDto;
import com.coopang.ainoti.presentation.request.ai.UpdateAiRequestHistoryRequestDto;
import com.coopang.apidata.application.ai.AiCategory;
import com.coopang.coredata.user.enums.UserRoleEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "AiRequestHistoryController API", description = "AI 요청 기록 관리 API")
@Slf4j(topic = "AiRequestHistoryController")
@RestController
@RequestMapping("/ai-request-histories/v1/ai-request-history")
public class AiRequestHistoryController {

    private final AiRequestHistoryService aiRequestHistoryService;

    public AiRequestHistoryController(AiRequestHistoryService aiRequestHistoryService) {
        this.aiRequestHistoryService = aiRequestHistoryService;
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
        AiRequestHistoryDto aiRequestHistoryDto = AiRequestHistoryDto.of(
            null
            , !StringUtils.isBlank(req.getAiCategory()) ? AiCategory.getAiCategoryEnum(req.getAiCategory()) : null
            , req.getAiRequest()
            , req.getAiResponse()
        );
        final AiRequestHistoryResponseDto aiRequestHistory = aiRequestHistoryService.createAiRequestHistory(aiRequestHistoryDto);
        return new ResponseEntity<>(aiRequestHistory, HttpStatus.CREATED);
    }

    /**
     * 단일 AI 요청 기록 조회
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @GetMapping("/{aiRequestHistoryId}")
    public ResponseEntity<AiRequestHistoryResponseDto> getAiRequestHistoryInfo(@PathVariable UUID aiRequestHistoryId) {
        final AiRequestHistoryResponseDto aiRequestHistory = aiRequestHistoryService.getValidAiRequestHistoryById(aiRequestHistoryId);
        return new ResponseEntity<>(aiRequestHistory, HttpStatus.OK);
    }

    /**
     * AI 요청 기록 검색 (페이징, 정렬, 키워드 검색)
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @GetMapping("/search")
    public ResponseEntity<Page<AiRequestHistoryResponseDto>> searchAiRequestHistories(
        @RequestParam(value = "aiRequestHistoryId", required = false) UUID aiRequestHistoryId
        , @RequestParam(value = "aiCategory", required = false) String aiCategory
        , @RequestParam(value = "aiRequest", required = false) String aiRequest
        , @RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted
        , Pageable pageable
    ) {
        final AiRequestHistorySearchConditionDto condition = AiRequestHistorySearchConditionDto.of(
            aiRequestHistoryId
            , aiCategory
            , aiRequest
            , isDeleted
        );
        final Page<AiRequestHistoryResponseDto> responsePage = aiRequestHistoryService.searchAiRequestHistories(condition, pageable);
        return new ResponseEntity<>(responsePage, HttpStatus.OK);
    }

    /**
     * AI 요청 기록 리스트 조회
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @PostMapping("/list")
    public ResponseEntity<List<AiRequestHistoryResponseDto>> getAiRequestHistoryList(@RequestBody(required = false) AiRequestHistorySearchConditionRequestDto req) {
        AiRequestHistorySearchConditionDto condition;
        if (ObjectUtils.isEmpty(req)) {
            condition = AiRequestHistorySearchConditionDto.empty();
        } else {
            condition = AiRequestHistorySearchConditionDto.of(
                req.getAiRequestHistoryId()
                , req.getAiCategory()
                , req.getAiRequest()
                , req.isDeleted()
            );
        }
        final List<AiRequestHistoryResponseDto> aiRequestHistoryList = aiRequestHistoryService.getAiRequestHistoryList(condition);
        return new ResponseEntity<>(aiRequestHistoryList, HttpStatus.OK);
    }

    /**
     * AI 요청 기록 수정
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @PutMapping("/{aiRequestHistoryId}")
    public ResponseEntity<AiRequestHistoryResponseDto> updateAiRequestHistory(@PathVariable UUID aiRequestHistoryId, @Valid @RequestBody UpdateAiRequestHistoryRequestDto req) {
        final AiRequestHistoryDto aiRequestHistoryDto = AiRequestHistoryDto.of(
            aiRequestHistoryId
            , !StringUtils.isBlank(req.getAiCategory()) ? AiCategory.getAiCategoryEnum(req.getAiCategory()) : null
            , req.getAiRequest()
            , req.getAiResponse()
        );

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
