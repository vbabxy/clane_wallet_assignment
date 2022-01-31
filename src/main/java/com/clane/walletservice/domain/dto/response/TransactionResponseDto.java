package com.clane.walletservice.domain.dto.response;

import com.clane.walletservice.domain.dto.TransactionDetailDto;
import com.clane.walletservice.domain.dto.TransactionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDto implements Serializable {

    private TransactionDto transactionDto;

    private List<TransactionDetailDto> transactionDetails;
}
