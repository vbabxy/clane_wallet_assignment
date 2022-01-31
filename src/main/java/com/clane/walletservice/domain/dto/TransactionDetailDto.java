package com.clane.walletservice.domain.dto;


import com.clane.walletservice.domain.enums.TransactionClass;
import com.clane.walletservice.model.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetailDto implements Serializable {

    private BigDecimal finalBalance;
    private BigDecimal startingBalance;
    private TransactionClass transactionClass;
    private WalletDto walletDto;

}
