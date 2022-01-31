package com.clane.walletservice.controller;


import com.clane.walletservice.domain.dto.response.AppResponse;
import com.clane.walletservice.service.KycService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.clane.walletservice.controller.BaseApiController.KYC;

@RestController
@Slf4j
@Api(value = BaseApiController.BASE_API_PATH+ KYC, tags = "kyc-controller")
@RequestMapping(BaseApiController.BASE_API_PATH +KYC)
public class KYCController {

    private final KycService kycService;


    public KYCController(KycService kycService) {
        this.kycService = kycService;
    }

    @GetMapping(value ="/name/{kycName}")
    @ApiOperation(value = "Get Kyc details by name",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = AppResponse.class)
    public ResponseEntity<?> getKycByName(@PathVariable("kycName") String kycName) {

        var response = kycService.getKyc(kycName);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    @ApiOperation(value = "Get all Kyc details ",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = AppResponse.class)
    public ResponseEntity<?> getAllKycs() {

        var response = kycService.getKycs();

        return ResponseEntity.ok().body(response);
    }
}
