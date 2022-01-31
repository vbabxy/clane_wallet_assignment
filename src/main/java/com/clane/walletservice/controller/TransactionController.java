package com.clane.walletservice.controller;


import com.clane.walletservice.domain.dto.request.TransactionRequestDto;
import com.clane.walletservice.domain.dto.request.WalletRequestDto;
import com.clane.walletservice.domain.dto.response.AppResponse;
import com.clane.walletservice.domain.enums.TransactionType;
import com.clane.walletservice.domain.enums.TransferMeans;
import com.clane.walletservice.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.clane.walletservice.controller.BaseApiController.TRANSACTION;


@RestController
@Slf4j
@Api(value = BaseApiController.BASE_API_PATH+ TRANSACTION, tags = "transaction-controller")
@RequestMapping(BaseApiController.BASE_API_PATH +TRANSACTION)
public class TransactionController {

    private final TransactionService transactionService;


    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @PutMapping("/transactionType/{transactionType}/trannsferMeans/{transferMeans}")
    @ApiOperation(value = "Perform Either Wallet to wallet or TopUp using Transaction Types (TRANSFER,TOP_UP)",
            notes = "Funds can be sent to destination wallet using a Transfer means (EMAIL, ACCOUNT_NUMBER)",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = AppResponse.class)
    public ResponseEntity<?> createWallet(@RequestBody @Valid TransactionRequestDto requestDto,
                                          @PathVariable("transactionType")TransactionType transactionType,
                                          @PathVariable("transferMeans")TransferMeans transferMeans) {

        var response = transactionService.performTransaction(requestDto,
                transactionType, transferMeans);
        return ResponseEntity.ok().body(response);
    }
}
