package com.clane.walletservice.service;

import com.clane.walletservice.domain.dto.KycDto;
import com.clane.walletservice.domain.dto.response.AppResponse;
import com.clane.walletservice.model.Kyc;

import java.util.List;

public interface KycService {

    AppResponse<KycDto> getKyc(String kycName);

    AppResponse<List<KycDto>>getKycs();

    Kyc getKycByName(String kycName);

}
