package com.coopang.hub.presentation.controller.user;

import com.coopang.apicommunication.feignclient.user.UserClientController;
import com.coopang.apicommunication.feignclient.user.UserClientService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserClientControllerImpl extends UserClientController {
    protected UserClientControllerImpl(UserClientService userClientService) {
        super(userClientService);
    }
}