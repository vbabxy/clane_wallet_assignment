package com.clane.walletservice.service.impl;

import com.clane.walletservice.domain.dto.TransactionDetailDto;
import com.clane.walletservice.domain.dto.TransactionDto;
import com.clane.walletservice.domain.dto.WalletDto;
import com.clane.walletservice.domain.dto.request.TransactionRequestDto;
import com.clane.walletservice.domain.dto.response.APIResponseMessages;
import com.clane.walletservice.domain.dto.response.AppResponse;
import com.clane.walletservice.domain.dto.response.TransactionResponseDto;
import com.clane.walletservice.domain.enums.TransactionType;
import com.clane.walletservice.domain.enums.TransferMeans;
import com.clane.walletservice.exception.BadRequestException;
import com.clane.walletservice.domain.enums.TransactionClass;
import com.clane.walletservice.domain.enums.WalletStatus;
import com.clane.walletservice.model.Kyc;
import com.clane.walletservice.model.Transaction;
import com.clane.walletservice.model.TransactionDetail;
import com.clane.walletservice.model.Wallet;
import com.clane.walletservice.repositories.TransactionDetailRepository;
import com.clane.walletservice.repositories.TransactionRepository;
import com.clane.walletservice.service.TransactionService;
import com.clane.walletservice.service.WalletService;
import com.clane.walletservice.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final WalletService walletService;
    private final ModelMapper modelMapper;
    private final TransactionRepository transactionRepository;
    private final TransactionDetailRepository transactionDetailRepository;

    public TransactionServiceImpl(WalletService walletService, ModelMapper modelMapper,
                                  TransactionRepository transactionRepository,
                                  TransactionDetailRepository transactionDetailRepository) {
        this.walletService = walletService;
        this.modelMapper = modelMapper;
        this.transactionRepository = transactionRepository;
        this.transactionDetailRepository = transactionDetailRepository;
    }


    @Override
    public AppResponse<TransactionResponseDto> performTransaction(TransactionRequestDto requestDto,
                                                                  TransactionType transactionType,
                                                                  TransferMeans transferMeans) {
        log.info("about to perform a transaction based onn the transaction type {} ",
                transactionType);

        TransactionResponseDto transactionResponseDto;

        switch (transactionType) {
            case TOP_UP:
                transactionResponseDto = performTopUp(requestDto, transactionType, transferMeans);
                break;
            case TRANSFER:
                transactionResponseDto = performTransfer(requestDto, transactionType, transferMeans);
                break;
            default:
                throw new BadRequestException("Transaction type not supported");
        }

        return AppResponse.<TransactionResponseDto>builder()
                .status(HttpStatus.OK.value())
                .data(transactionResponseDto)
                .message(APIResponseMessages.SUCCESSFUL)
                .build();
    }

    private TransactionResponseDto performTopUp(TransactionRequestDto requestDto,
                                                TransactionType transactionType,
                                                TransferMeans transferMeans) {
        log.info("about to perform top  up transaction {} ", requestDto.getReceiverWalletIdentifier());

        Wallet receiverWallet = getReceiverWallet(requestDto, transferMeans);

        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionType);
        transaction.setTransactionReference(Utils.generateReference());
        transaction.setAmount(requestDto.getTransactionAmount());

        transaction =   transactionRepository.save(transaction);

        log.info("Transaction Saved successfully {} ", transaction.getId());

        var creditTransactionDetail = creditTransaction(transaction, receiverWallet);

        receiverWallet.setBalance(creditTransactionDetail.getFinalBalance());
        walletService.updateWallet(receiverWallet);

        TransactionDetailDto transactionDetailDto =
                modelMapper.map(creditTransactionDetail, TransactionDetailDto.class);

        List<TransactionDetailDto> transactionDetailDtos = new ArrayList<>();
        transactionDetailDtos.add(transactionDetailDto);

        return TransactionResponseDto.builder()
                .transactionDto(modelMapper.map(transaction, TransactionDto.class))
                .transactionDetails(transactionDetailDtos)
                .build();
    }

    private TransactionResponseDto performTransfer(TransactionRequestDto requestDto,
                                                   TransactionType transactionType,
                                                   TransferMeans transferMeans) {
        log.info("about to perform wallet tp wallet transaction {} ", requestDto);

        if(StringUtils.isBlank(requestDto.getSenderWalletAccountNumber())) {
            throw new BadRequestException("Sender wallet must be provided");
        }
        var senderWallet = walletService.findWalletByAccountNumber(requestDto.getSenderWalletAccountNumber());

        validateSenderTransaction(requestDto.getTransactionAmount(), senderWallet);

        var receiverWallet = getReceiverWallet(requestDto, transferMeans);

        if(senderWallet.getId() == receiverWallet.getId()) {
            throw new BadRequestException("Self Transfer is not allowed");
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionType);
        transaction.setTransactionReference(Utils.generateReference());
        transaction.setAmount(requestDto.getTransactionAmount());

        transaction = transactionRepository.save(transaction);

        TransactionDetail debitTransactionDetail = debitSenderWallet(transaction,senderWallet);
        TransactionDetail creditTransactionDetail = creditReceiverWallet(transaction, receiverWallet);

        return buildTransferTransactionDetailsDtos(debitTransactionDetail,
                creditTransactionDetail, transaction);

    }

    private Wallet getReceiverWallet(TransactionRequestDto requestDto, TransferMeans transferMeans) {

        Wallet wallet;

        switch (transferMeans) {
            case EMAIL:
                wallet = walletService.findWalletByEmail(requestDto.getReceiverWalletIdentifier());
                break;
            case ACCOUNT_NUMBER:
                wallet = walletService.findWalletByAccountNumber(requestDto.getReceiverWalletIdentifier());
                break;
            default:
                throw new BadRequestException("Invalid Transfer means");
        }

        return wallet;
    }

    private TransactionDetail creditReceiverWallet(Transaction transaction, Wallet receiverWallet) {
        return creditTransaction(transaction, receiverWallet);

    }

    private TransactionDetail creditTransaction(Transaction transaction, Wallet receiverWallet) {
        TransactionDetail creditTransactionDetail = new TransactionDetail();
        creditTransactionDetail.setTransaction(transaction);
        creditTransactionDetail.setTransactionClass(TransactionClass.CREDIT);
        creditTransactionDetail.setWallet(receiverWallet);
        creditTransactionDetail.setStartingBalance(receiverWallet.getBalance());
        creditTransactionDetail.setFinalBalance(receiverWallet.getBalance().add(transaction.getAmount()));
        creditTransactionDetail =  transactionDetailRepository.save(creditTransactionDetail);

        receiverWallet.setBalance(creditTransactionDetail.getFinalBalance());
        walletService.updateWallet(receiverWallet);

        return creditTransactionDetail;
    }

    private TransactionDetail debitSenderWallet(Transaction transaction, Wallet senderWallet) {

        senderWallet.setBalance(senderWallet.getBalance().subtract(transaction.getAmount()));
        walletService.updateWallet(senderWallet);

        TransactionDetail debitTransactionDetail = new TransactionDetail();
        debitTransactionDetail.setTransaction(transaction);
        debitTransactionDetail.setTransactionClass(TransactionClass.DEBIT);
        debitTransactionDetail.setWallet(senderWallet);
        debitTransactionDetail.setStartingBalance(senderWallet.getBalance());
        debitTransactionDetail.setFinalBalance(senderWallet.getBalance().subtract(transaction.getAmount()));
        debitTransactionDetail =  transactionDetailRepository.save(debitTransactionDetail);
        return debitTransactionDetail;
    }

    private void validateSenderTransaction(BigDecimal transactionAmount, Wallet senderWallet) {
        log.info("about to  validate wallet transaction limit {} ", transactionAmount);

        if(senderWallet.getStatus().equals(WalletStatus.INACTIVE)) {
            throw new BadRequestException("Wallet must be activated before performing wallet to wallet transaction");
        }

        Kyc walletKyc = senderWallet.getUser().getKyc();

        if(transactionAmount.compareTo(walletKyc.getTransactionLimit()) > 0) {
            throw new BadRequestException("Wallet cannot transfer above transaction limit :: "
                    + walletKyc.getTransactionLimit() +"please upgrade your account");
        }

        if(transactionAmount.compareTo(senderWallet.getBalance()) > 0) {
            throw new BadRequestException("Insufficient funds, please fund your wallet");
        }
    }

    private TransactionResponseDto buildTransferTransactionDetailsDtos(TransactionDetail debitTransactionDetail,
                                                                       TransactionDetail creditTransactionDetail,
                                                                       Transaction transaction) {
        WalletDto debitWalletDto = modelMapper.map(debitTransactionDetail.getWallet(), WalletDto.class);
        TransactionDetailDto debitTransactionDetailDto =
                modelMapper.map(debitTransactionDetail, TransactionDetailDto.class);
        debitTransactionDetailDto.setWalletDto(debitWalletDto);

        WalletDto creditWalletDto = modelMapper.map(creditTransactionDetail.getWallet(), WalletDto.class);
        TransactionDetailDto creditTransactionDetailDto =
                modelMapper.map(creditTransactionDetail, TransactionDetailDto.class);
        creditTransactionDetailDto.setWalletDto(creditWalletDto);

        List<TransactionDetailDto> transactionDetailDtos = new ArrayList<>();
        transactionDetailDtos.add(debitTransactionDetailDto);
        transactionDetailDtos.add(creditTransactionDetailDto);

        return TransactionResponseDto.builder()
                .transactionDto(modelMapper.map(transaction, TransactionDto.class))
                .transactionDetails(transactionDetailDtos)
                .build();
    }
}
