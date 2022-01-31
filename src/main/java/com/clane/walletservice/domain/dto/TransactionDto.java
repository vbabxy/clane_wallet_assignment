package com.clane.walletservice.domain.dto;

import com.clane.walletservice.domain.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    private Timestamp createdAt;
    private BigDecimal amount;
    private TransactionType transactionType;
    private String transactionReference;
}
