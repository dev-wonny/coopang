package com.coopang.hub.presentation.controller.hub;

import com.coopang.apicommunication.feignclient.hub.HubClientController;
import com.coopang.apicommunication.feignclient.hub.HubClientService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HubClientControllerImpl extends HubClientController {
    public HubClientControllerImpl(HubClientService hubClientService) {
        super(hubClientService);
    }
}