package com.clane.walletservice.domain.dto.request;

import com.clane.walletservice.domain.enums.TransactionType;
import com.clane.walletservice.domain.enums.TransferMeans;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDto implements Serializable {

    @NotBlank(message = "Either email or account number must be provided")
    private String receiverWalletIdentifier;

    private String senderWalletAccountNumber;

    @NotNull(message = "transaction amount must be provided")
    @Positive(message = "amount must be greater than zero")
    private BigDecimal transactionAmount;
}
