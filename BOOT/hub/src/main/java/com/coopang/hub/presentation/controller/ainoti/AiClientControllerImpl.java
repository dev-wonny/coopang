package com.coopang.hub.presentation.controller.ainoti;

import com.coopang.apicommunication.feignclient.ai.AiClientController;
import com.coopang.apicommunication.feignclient.ai.AiClientService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiClientControllerImpl extends AiClientController {
    public AiClientControllerImpl(AiClientService aiClientService) {
        super(aiClientService);
    }
}