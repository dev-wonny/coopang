package com.coopang.apicommunication.feignclient.shipper;

import com.coopang.apiconfig.feignclient.FeignConfig;
import com.coopang.apidata.application.shipper.request.ShipperSearchConditionRequest;
import com.coopang.apidata.application.shipper.response.ShipperResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "hub", configuration = FeignConfig.class)
public interface ShipperClient {
    @GetMapping("/shippers/v1/shipper/{shipperId}")
    ShipperResponse getShipperInfo(@PathVariable("shipperId") UUID shipperId);

    @PostMapping("/shippers/v1/shipper/list")
    List<ShipperResponse> getShipperList(@RequestBody ShipperSearchConditionRequest req);
}
