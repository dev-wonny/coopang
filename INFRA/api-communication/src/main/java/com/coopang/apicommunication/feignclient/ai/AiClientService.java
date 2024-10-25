package com.coopang.apicommunication.feignclient.ai;

import com.coopang.apidata.application.ai.request.AiRequestHistorySearchConditionRequest;
import com.coopang.apidata.application.ai.request.CreateAiRequestHistoryRequest;
import com.coopang.apidata.application.ai.response.AiRequestHistoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AiClientService {
    private final AiClient aiClient;

    public AiClientService(AiClient aiClient) {
        this.aiClient = aiClient;
    }

    AiRequestHistoryResponse createAiRequestHistory(CreateAiRequestHistoryRequest req) {
        return aiClient.createAiRequestHistory(req);
    }

    AiRequestHistoryResponse getAiRequestHistoryInfo(UUID aiRequestHistoryId) {
        return aiClient.getAiRequestHistoryInfo(aiRequestHistoryId);
    }

    List<AiRequestHistoryResponse> getAiRequestHistoryList(AiRequestHistorySearchConditionRequest req) {
        return aiClient.getAiRequestHistoryList(req);
    }
}
