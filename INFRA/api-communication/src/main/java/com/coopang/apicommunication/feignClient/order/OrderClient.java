package com.coopang.apicommunication.feignclient.order;

import com.coopang.apiconfig.feignclient.FeignConfig;
import com.coopang.apidata.application.order.response.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "order", contextId = "orderClient", configuration = FeignConfig.class)
public interface OrderClient {
    @GetMapping("orders/v1/order/list")
    List<OrderResponse> getOrderList();
}
