package com.clane.walletservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KycDto {

    private Long id;

    private String levelName;

    private BigDecimal transactionLimit;
}
