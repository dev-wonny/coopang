package com.coopang.apicommunication.feignclient.ai;

import com.coopang.apidata.application.ai.request.AiRequestHistorySearchConditionRequest;
import com.coopang.apidata.application.ai.request.CreateAiRequestHistoryRequest;
import com.coopang.apidata.application.ai.response.AiRequestHistoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "ainoti", contextId = "aiClient", configuration = com.coopang.apiconfig.feignclient.FeignConfig.class)
public interface AiClient {
    @PostMapping("/ai-request-histories/v1/ai-request-history")
    AiRequestHistoryResponse createAiRequestHistory(CreateAiRequestHistoryRequest req);

    @GetMapping("/ai-request-histories/v1/ai-request-history/{aiRequestHistoryId}")
    AiRequestHistoryResponse getAiRequestHistoryInfo(@PathVariable("aiRequestHistoryId") UUID aiRequestHistoryId);

    @PostMapping("/ai-request-histories/v1/ai-request-history/list")
    List<AiRequestHistoryResponse> getAiRequestHistoryList(@RequestBody AiRequestHistorySearchConditionRequest req);
}