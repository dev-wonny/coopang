package com.coopang.order.presentation.controller.pg;

import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.order.application.service.pg.PGService;
import com.coopang.order.presentation.request.pg.PGRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "PGController API", description = "PGController API")
@Slf4j(topic = "PGController")
@RestController
@RequestMapping("/payments/v1/pg")
public class PGController {

    /*
    Todo : 결제 확인을 위한 PGPaymentId 를 임의의로 생성해서 전달
     */
    private final PGService pgService;

    public PGController(PGService pgService) {
        this.pgService = pgService;
    }

    @Secured(UserRoleEnum.Authority.MASTER)
    @PostMapping
    public ResponseEntity<String> tryPayPG(
            @RequestBody PGRequestDto requestDto
            ){
        final String result = pgService.processPaymentRequest(requestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured(UserRoleEnum.Authority.MASTER)
    @PostMapping("/cancel")
    public ResponseEntity<String> tryCancelPG(
            @RequestBody PGRequestDto requestDto
    ){
        final String result = pgService.cancelPaymentRequest(requestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
