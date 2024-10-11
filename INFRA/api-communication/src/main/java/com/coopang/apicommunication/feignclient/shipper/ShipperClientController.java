package com.coopang.apicommunication.feignclient.shipper;

import com.coopang.apidata.application.shipper.request.ShipperSearchConditionRequest;
import com.coopang.apidata.application.shipper.response.ShipperResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "ShipperClientController API", description = "ShipperClientController API")
@Slf4j(topic = "ShipperClientController")
@RestController
public class ShipperClientController {
    private final ShipperClientService shipperClientService;

    public ShipperClientController(ShipperClientService shipperClientService) {
        this.shipperClientService = shipperClientService;
    }

    @GetMapping("/{shipperId}")
    ShipperResponse getShipperInfo(@PathVariable("shipperId") UUID shipperId) {
        return shipperClientService.getShipperInfo(shipperId);
    }

    @PostMapping("/list")
    List<ShipperResponse> getShipperList(@RequestBody ShipperSearchConditionRequest req) {
        return shipperClientService.getShipperList(req);
    }
}
