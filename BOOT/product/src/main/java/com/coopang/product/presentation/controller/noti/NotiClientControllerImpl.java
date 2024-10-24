package com.coopang.product.presentation.controller.noti;

import com.coopang.apicommunication.feignclient.noti.NotiClientController;
import com.coopang.apicommunication.feignclient.noti.NotiClientService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotiClientControllerImpl extends NotiClientController {
    public NotiClientControllerImpl(NotiClientService notiClientService) {
        super(notiClientService);
    }
}