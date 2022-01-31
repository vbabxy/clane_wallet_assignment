package com.clane.walletservice.domain.dto;

import com.clane.walletservice.domain.enums.WalletStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletDto implements Serializable {

    private WalletStatus status;
    private String walletAccountNumber;
    private BigDecimal balance;
    private Long id;
    private Timestamp createdAt;
}
