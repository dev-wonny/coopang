package com.coopang.apicommunication.feignclient.order;

import com.coopang.apidata.application.order.response.OrderResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "OrderClientController API", description = "OrderClientController API")
@Slf4j(topic = "OrderClientController")
@RequestMapping("/feignClient/v1/order")
public abstract class OrderClientController {
    private final OrderClientService orderClientService;

    protected OrderClientController(OrderClientService orderClientService) {
        this.orderClientService = orderClientService;
    }

    @GetMapping("/list")
    public List<OrderResponse> getOrderList(){
        return orderClientService.getOrderList();
    }
}
