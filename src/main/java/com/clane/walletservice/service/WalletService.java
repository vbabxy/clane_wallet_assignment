package com.clane.walletservice.service;

import com.clane.walletservice.domain.dto.WalletDto;
import com.clane.walletservice.domain.dto.request.WalletRequestDto;
import com.clane.walletservice.domain.dto.response.AppResponse;
import com.clane.walletservice.domain.enums.WalletStatus;
import com.clane.walletservice.model.Wallet;
import org.springframework.data.domain.Page;

public interface WalletService {

    AppResponse<WalletDto> createWallet(WalletRequestDto walletRequestDto);

    AppResponse<WalletDto> updateWalletStatus(WalletStatus status, Long walletId);

    AppResponse<Page<WalletDto>> getWallets(String walletAccountNumber, int page, int size);

    Wallet findWalletByAccountNumber(String walletIdentifier);

    Wallet updateWallet(Wallet receiverWallet);

    Wallet findWalletByEmail(String walletIdentifier);
}
