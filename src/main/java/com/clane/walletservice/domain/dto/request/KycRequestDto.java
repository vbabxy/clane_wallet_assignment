package com.clane.walletservice.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KycRequestDto {

    @NotBlank(message = "kyc name must be provided")
    private String kycName;

    @NotNull(message = "transaction limit must be provided")
    private BigDecimal transactionLimit;
}
