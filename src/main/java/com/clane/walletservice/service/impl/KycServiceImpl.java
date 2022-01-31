package com.clane.walletservice.service.impl;

import com.clane.walletservice.domain.dto.KycDto;
import com.clane.walletservice.domain.dto.response.APIResponseMessages;
import com.clane.walletservice.domain.dto.response.AppResponse;
import com.clane.walletservice.exception.ModelNotFoundException;
import com.clane.walletservice.model.Kyc;
import com.clane.walletservice.repositories.KycRepository;
import com.clane.walletservice.service.KycService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class KycServiceImpl implements KycService {

    private final KycRepository kycRepository;
    private final ModelMapper modelMapper;

    public KycServiceImpl(KycRepository kycRepository, ModelMapper modelMapper) {
        this.kycRepository = kycRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public AppResponse<KycDto> getKyc(String kycName) {
        log.info("about to get the kyc details {} ", kycName);

        Kyc kyc = getKycByName(kycName);
        KycDto kycDto = modelMapper.map(kyc, KycDto.class);

        return AppResponse.<KycDto>builder()
                .status(HttpStatus.OK.value())
                .message(APIResponseMessages.SUCCESSFUL)
                .data(kycDto)
                .build();
    }

    @Override
    public AppResponse<List<KycDto>> getKycs() {
        log.info("about to get all kycs");

        List<Kyc> kycs = kycRepository.findAll();

        List<KycDto> kycDtos = new ArrayList<>();

        if(!kycs.isEmpty()) {
            kycDtos = kycs.stream().map(kyc -> modelMapper.map(kyc, KycDto.class))
                    .collect(Collectors.toList());
        }

        return AppResponse.<List<KycDto>>builder()
                .message(APIResponseMessages.GET)
                .data(kycDtos)
                .status(HttpStatus.OK.value())
                .build();
    }

    @Override
    public Kyc getKycByName(String kycName) {
        return kycRepository.findByLevelName(kycName)
                .orElseThrow(() -> new ModelNotFoundException("kyc does not exist"));
    }
}
