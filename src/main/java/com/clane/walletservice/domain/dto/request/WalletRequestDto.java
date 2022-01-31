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
public class WalletRequestDto {

    @NotBlank(message = "user email address must be provided")
    private String emailAddress;
}
