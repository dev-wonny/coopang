package com.coopang.apicommunication.feignclient.order;

import com.coopang.apidata.application.order.response.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderClientService {
    private final OrderClient orderClient;

    public OrderClientService(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    public List<OrderResponse> getOrderList() {
        return orderClient.getOrderList();
    }
}
