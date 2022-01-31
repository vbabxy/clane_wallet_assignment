package com.clane.walletservice.controller;

import com.clane.walletservice.domain.dto.request.CreateUserRequestDto;
import com.clane.walletservice.domain.dto.request.WalletRequestDto;
import com.clane.walletservice.domain.dto.response.AppResponse;
import com.clane.walletservice.domain.enums.WalletStatus;
import com.clane.walletservice.service.WalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.clane.walletservice.controller.BaseApiController.*;

@RestController
@Slf4j
@Api(value = BaseApiController.BASE_API_PATH+ WALLET, tags = "wallet-controller")
@RequestMapping(BaseApiController.BASE_API_PATH +WALLET)
public class WalletController {

    private final WalletService walletService;


    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    @ApiOperation(value = "Create new wallet for user",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = AppResponse.class)
    public ResponseEntity<?> createWallet(@RequestBody @Valid WalletRequestDto walletRequestDto) {

        var response = walletService.createWallet(walletRequestDto);
        return ResponseEntity.ok().body(response);
    }


    @PatchMapping("/walletId/{walletId}/walletStatus/{walletStatus}")
    @ApiOperation(value = "update wallet status",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = AppResponse.class)
    public ResponseEntity<?> updateWallet(@PathVariable("walletId") Long walletId,
                                          @PathVariable("walletStatus")WalletStatus walletStatus) {

        var response = walletService.updateWalletStatus(walletStatus, walletId);
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/walletAccountNumber/{walletAccountNumber}")
    @ApiOperation(value = "Get All wallets",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = AppResponse.class)
    public ResponseEntity<?> getWallets(@PathVariable("walletAccountNumber") String walletAccountNumber,
                                          @RequestParam(value = "page") int page,
                                          @RequestParam(value = "size") int size) {

        var response = walletService.getWallets(walletAccountNumber, page, size);
        return ResponseEntity.ok().body(response);
    }

}
