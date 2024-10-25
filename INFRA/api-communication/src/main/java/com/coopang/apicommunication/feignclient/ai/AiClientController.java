package com.coopang.apicommunication.feignclient.ai;

import com.coopang.apidata.application.ai.request.AiRequestHistorySearchConditionRequest;
import com.coopang.apidata.application.ai.request.CreateAiRequestHistoryRequest;
import com.coopang.apidata.application.ai.response.AiRequestHistoryResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Tag(name = "AiClientController API", description = "AiClientController API")
@Slf4j(topic = "AiClientController")
@RequestMapping("/feignClient/v1/ai")
public class AiClientController {
    private final AiClientService aiClientService;

    public AiClientController(AiClientService aiClientService) {
        this.aiClientService = aiClientService;
    }

    @PostMapping
    AiRequestHistoryResponse createAiRequestHistory(CreateAiRequestHistoryRequest req) {
        return aiClientService.createAiRequestHistory(req);

    }

    @GetMapping("/{aiRequestHistoryId}")
    AiRequestHistoryResponse getAiRequestHistoryInfo(@PathVariable("aiRequestHistoryId") UUID aiRequestHistoryId) {
        return aiClientService.getAiRequestHistoryInfo(aiRequestHistoryId);
    }

    @PostMapping("/list")
    List<AiRequestHistoryResponse> getAiRequestHistoryList(@RequestBody AiRequestHistorySearchConditionRequest req) {
        return aiClientService.getAiRequestHistoryList(req);
    }
}
