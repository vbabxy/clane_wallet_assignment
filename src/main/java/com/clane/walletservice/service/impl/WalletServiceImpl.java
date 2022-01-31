package com.clane.walletservice.service.impl;

import com.clane.walletservice.domain.dto.WalletDto;
import com.clane.walletservice.domain.dto.request.WalletRequestDto;
import com.clane.walletservice.domain.dto.response.*;
import com.clane.walletservice.domain.enums.WalletStatus;
import com.clane.walletservice.exception.ModelAlreadyExistException;
import com.clane.walletservice.exception.ModelNotFoundException;
import com.clane.walletservice.model.Wallet;
import com.clane.walletservice.repositories.WalletRepository;
import com.clane.walletservice.service.UserService;
import com.clane.walletservice.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.clane.walletservice.utils.Utils.generateRandomDigits;
import static com.clane.walletservice.utils.Utils.validatePagRequest;

@Service
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final UserService userService;
    private final WalletRepository  walletRepository;
    private final ModelMapper modelMapper;

    public WalletServiceImpl(UserService userService, WalletRepository walletRepository, ModelMapper modelMapper) {
        this.userService = userService;
        this.walletRepository = walletRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AppResponse<WalletDto> createWallet(WalletRequestDto walletRequestDto) {
        log.info("about to create wallet for user {} ", walletRequestDto.getEmailAddress());

        var user = userService.findUserByEmail(walletRequestDto.getEmailAddress());

        final var foundWallet = walletRepository.findWalletByUser(user);

        if(foundWallet.isPresent()) {
            throw new ModelAlreadyExistException("User already has a wallet");
        }

        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal("0.00"));
        wallet.setStatus(WalletStatus.INACTIVE);
        wallet.setWalletAccountNumber(generateRandomDigits());
        wallet.setUser(user);
        wallet = walletRepository.save(wallet);

        WalletDto walletDto = modelMapper.map(wallet, WalletDto.class);

        return AppResponse.<WalletDto>builder()
                .message(APIResponseMessages.SUCCESSFUL)
                .data(walletDto)
                .status(HttpStatus.CREATED.value())
                .build();
    }

    @Override
    public AppResponse<WalletDto> updateWalletStatus(WalletStatus status, Long walletId) {
        log.info("about to update wallet status {}  ", walletId);

        var foundWallet = walletRepository.findById(walletId)
                .orElseThrow(()-> new ModelNotFoundException("wallet does not exist"));

        foundWallet.setStatus(status);
        foundWallet = walletRepository.save(foundWallet);

        WalletDto walletDto = modelMapper.map(foundWallet, WalletDto.class);

        return AppResponse.<WalletDto>builder()
                .message(APIResponseMessages.SUCCESSFUL)
                .data(walletDto)
                .status(HttpStatus.CREATED.value())
                .build();
    }

    @Override
    public AppResponse<Page<WalletDto>> getWallets(String walletAccountNumber, int page, int size) {

        validatePagRequest(page, size);

        Pageable pageable = PageRequest.of(page - 1, size);
        List<WalletDto> walletDtos;
        Page<Wallet> wallets = walletRepository.findAllByWalletAccountNumberContaining(walletAccountNumber, pageable);

        walletDtos =  wallets.stream()
                .map(wallet -> modelMapper.map(wallet, WalletDto.class))
                .collect(Collectors.toList());

        var walletDtoPage = new PageImpl<>(walletDtos, wallets.getPageable(), wallets.getTotalElements());

        return AppResponse.<Page<WalletDto>>builder()
                .status(HttpStatus.OK.value())
                .data(walletDtoPage)
                .message(APIResponseMessages.GET)
                .build();
    }

    @Override
    public Wallet findWalletByAccountNumber(String walletIdentifier) {
        log.info("wallet identifier provided {}  ", walletIdentifier);

        return walletRepository
                .findWalletByWalletAccountNumber(walletIdentifier)
                .orElseThrow(()->
                        new ModelNotFoundException("Wallet does not exist with identifier :: "+walletIdentifier));
    }

    @Override
    public Wallet updateWallet(Wallet receiverWallet) {
        return walletRepository.save(receiverWallet);
    }

    @Override
    public Wallet findWalletByEmail(String receiverEmailAddress) {
        return walletRepository
                .findWalletByUser_EmailAddress(receiverEmailAddress)
                .orElseThrow(()->
                        new ModelNotFoundException("Wallet does not exist with identifier :: "+receiverEmailAddress));
    }


}
