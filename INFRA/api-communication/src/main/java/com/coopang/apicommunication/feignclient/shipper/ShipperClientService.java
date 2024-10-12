package com.coopang.apicommunication.feignclient.shipper;

import com.coopang.apidata.application.shipper.request.ShipperSearchConditionRequest;
import com.coopang.apidata.application.shipper.response.ShipperResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Service
public class ShipperClientService {
    private final ShipperClient shipperClient;

    public ShipperClientService(ShipperClient shipperClient) {
        this.shipperClient = shipperClient;
    }

    public ShipperResponse getShipperInfo(@PathVariable("shipperId") UUID shipperId) {
        return shipperClient.getShipperInfo(shipperId);
    }

    public List<ShipperResponse> getShipperList(ShipperSearchConditionRequest req) {
        return shipperClient.getShipperList(req);
    }
}