package com.coopang.apicommunication.feignclient.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "order", contextId = "orderClient", configuration = FeignClient.class)
public interface OrderClient {
    @GetMapping("orders/v1/order/list")
    List<OrderResponse> getOrderList();
}
