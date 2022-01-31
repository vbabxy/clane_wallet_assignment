package com.clane.walletservice.service;

import com.clane.walletservice.domain.dto.TransactionDetailDto;
import com.clane.walletservice.domain.dto.TransactionDto;
import com.clane.walletservice.domain.dto.request.TransactionRequestDto;
import com.clane.walletservice.domain.dto.response.AppResponse;
import com.clane.walletservice.domain.dto.response.TransactionResponseDto;
import com.clane.walletservice.domain.enums.TransactionType;
import com.clane.walletservice.domain.enums.TransferMeans;

import java.util.List;

public interface TransactionService {

    AppResponse<TransactionResponseDto> performTransaction(TransactionRequestDto requestDto,
                                                           TransactionType transactionType,
                                                           TransferMeans transferMeans);


}
