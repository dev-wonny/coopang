package com.coopang.order.presentation.controller.order;

import com.coopang.apicommunication.feignclient.order.OrderClientController;
import com.coopang.apicommunication.feignclient.order.OrderClientService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderClientControllerImpl extends OrderClientController {
    public OrderClientControllerImpl(OrderClientService orderClientService) {
        super(orderClientService);
    }
}
