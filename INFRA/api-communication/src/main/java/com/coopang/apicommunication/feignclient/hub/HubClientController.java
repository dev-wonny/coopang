package com.coopang.apicommunication.feignclient.hub;

import com.coopang.apidata.application.hub.request.HubSearchConditionRequest;
import com.coopang.apidata.application.hub.response.HubResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "HubClientController API", description = "HubClientController API")
@Slf4j(topic = "HubClientController")
@RequestMapping("/feignClient/v1/hub")
public class HubClientController {
    private final HubClientService hubClientService;

    public HubClientController(HubClientService hubClientService) {
        this.hubClientService = hubClientService;
    }

    @GetMapping("/{hubId}")
    public HubResponse getHubInfo(@PathVariable("hubId") UUID hubId) {
        return hubClientService.getHubInfo(hubId);
    }

    @PostMapping("/list")
    public List<HubResponse> getHubList(@RequestBody HubSearchConditionRequest req) {
        return hubClientService.getHubList(req);
    }
}