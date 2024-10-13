package com.coopang.hub.presentation.controller.shipper;

import com.coopang.apicommunication.feignclient.shipper.ShipperClientController;
import com.coopang.apicommunication.feignclient.shipper.ShipperClientService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShipperClientControllerImpl extends ShipperClientController {
    protected ShipperClientControllerImpl(ShipperClientService shipperClientService) {
        super(shipperClientService);
    }
}
