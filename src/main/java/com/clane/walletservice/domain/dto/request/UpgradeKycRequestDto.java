package com.clane.walletservice.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpgradeKycRequestDto {

    private String houseAddress;
    private String identityDocUrl;
    @NotBlank(message = "kyc name must be provided")
    private String kycName;
    private String bvn;

}
