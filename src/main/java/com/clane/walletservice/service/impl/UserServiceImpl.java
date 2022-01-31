package com.clane.walletservice.service.impl;

import com.clane.walletservice.domain.dto.UserDto;
import com.clane.walletservice.domain.dto.request.CreateUserRequestDto;
import com.clane.walletservice.domain.dto.request.UpgradeKycRequestDto;
import com.clane.walletservice.domain.dto.response.*;
import com.clane.walletservice.domain.enums.KYCLevels;
import com.clane.walletservice.exception.BadRequestException;
import com.clane.walletservice.exception.ModelAlreadyExistException;
import com.clane.walletservice.exception.ModelNotFoundException;
import com.clane.walletservice.model.Kyc;
import com.clane.walletservice.model.User;
import com.clane.walletservice.repositories.UserRepository;
import com.clane.walletservice.service.KycService;
import com.clane.walletservice.service.UserService;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final KycService kycService;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, KycService kycService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.kycService = kycService;
    }

    @Override
    public AppResponse<UserDto> createNewUser(CreateUserRequestDto createUserRequestDto) {
        log.info("about to create new user {} ", createUserRequestDto.getEmailAddress());

        Optional<User> emailAlreadyExist = userRepository
                .findByEmailAddress(createUserRequestDto.getEmailAddress());

        if(emailAlreadyExist.isPresent()) {
            throw new ModelAlreadyExistException("user already created with email address :: "
                    +createUserRequestDto.getEmailAddress());
        }

        Optional<User> phoneNumberAlreadyExist = userRepository.findByPhoneNumber(createUserRequestDto.getPhoneNumber());

        if(phoneNumberAlreadyExist.isPresent()) {
            throw new ModelAlreadyExistException("user already created with phone number :: "
                    +createUserRequestDto.getPhoneNumber());
        }

        var kyc = kycService.getKycByName(KYCLevels.KYC0.name());

        User savedUser = new User();
        savedUser.setEmailAddress(createUserRequestDto.getEmailAddress());
        savedUser.setEmailConfirmed(false);
        savedUser.setFirstName(createUserRequestDto.getFirstName());
        savedUser.setLastName(createUserRequestDto.getFirstName());
        savedUser.setKyc(kyc);
        savedUser.setHouseAddress(createUserRequestDto.getHouseAddress());
        savedUser.setPhoneNumber(createUserRequestDto.getPhoneNumber());

        savedUser = userRepository.save(savedUser);

        UserDto userDto = modelMapper.map(savedUser,  UserDto.class);

        return AppResponse.<UserDto>builder()
                .status(HttpStatus.CREATED.value())
                .data(userDto)
                .message(APIResponseMessages.SUCCESSFUL)
                .build();
    }

    @Override
    public AppResponse<UserDto> confirmEmailAddress(String emailAddress) {
        User foundUser = findUserByEmail(emailAddress);

        if(foundUser.isEmailConfirmed()) {
            throw new ModelAlreadyExistException("email already confirmed");
        }
        var kyc = kycService.getKycByName(KYCLevels.KYC1.name());
        foundUser.setKyc(kyc);
        foundUser.setEmailConfirmed(true);

        foundUser = userRepository.save(foundUser);

        UserDto userDto = modelMapper.map(foundUser,  UserDto.class);

        return AppResponse.<UserDto>builder()
                .status(HttpStatus.OK.value())
                .data(userDto)
                .message(APIResponseMessages.GET)
                .build();
    }

    @Override
    public AppResponse<UserDto> upgradeUserKyc(UpgradeKycRequestDto upgradeKycRequestDto,
                                               String emailAddress) {
        log.info("about to upgrade user kyc {} ", upgradeKycRequestDto);

        User foundUser = findUserByEmail(emailAddress);

        var kyc = kycService.getKycByName(upgradeKycRequestDto.getKycName());

        UserDto userDto;

        switch(kyc.getLevelName()) {
            case "KYC1":
                userDto = upgradeToKyc(kyc, foundUser);
                break;
            case "KYC2":
                userDto = upgradeToKyc(kyc, foundUser, upgradeKycRequestDto.getBvn());
                break;
            case "KYC3":
                userDto = upgradeToKyc(kyc, foundUser, upgradeKycRequestDto.getBvn(), upgradeKycRequestDto.getIdentityDocUrl());
                break;
            default:
                throw new ModelNotFoundException("Kyc does not exist");
        }

        return AppResponse.<UserDto>builder()
                .status(HttpStatus.OK.value())
                .data(userDto)
                .message(APIResponseMessages.UPDATE)
                .build();
    }

    @Override
    public User findUserByEmail(String emailAddress) {
        return userRepository
                .findByEmailAddress(emailAddress)
                .orElseThrow(() -> new ModelNotFoundException("user with email :: "+emailAddress+ " :: does not exist"));
    }

    private UserDto upgradeToKyc(Kyc kyc, User foundUser) {
        if (!foundUser.isEmailConfirmed()) {
            foundUser.setEmailConfirmed(true);
            foundUser.setKyc(kyc);
            foundUser = userRepository.save(foundUser);
        }
        return modelMapper.map(foundUser, UserDto.class);
    }

    private UserDto upgradeToKyc(Kyc kyc, User foundUser, String bvn) {
        if(!foundUser.isEmailConfirmed()) {
            throw new BadRequestException("User must confirm email before upgrading kyc level");
        }

        if(StringUtils.isBlank(bvn)) {
            throw new BadRequestException("User must provide bvn before upgrading kyc level");
        }

        foundUser.setBvn(bvn);
        foundUser.setKyc(kyc);
        foundUser = userRepository.save(foundUser);
        return modelMapper.map(foundUser, UserDto.class);
    }

    private UserDto upgradeToKyc(Kyc kyc, User foundUser, String bvn, String identityDocUrl) {

        if(!foundUser.isEmailConfirmed()) {
            throw new BadRequestException("User must confirm email before upgrading kyc level");
        }

        if(StringUtils.isBlank(bvn)) {
            if(StringUtils.isBlank(foundUser.getBvn()) || foundUser.getBvn() == null) {
                throw new BadRequestException("User must provide bvn before upgrading kyc level");
            }
        }else if(StringUtils.isNotBlank(bvn) &&
                (StringUtils.isBlank(foundUser.getBvn()) || foundUser.getBvn() == null)) {
            foundUser.setBvn(bvn);
        }

        if(StringUtils.isBlank(identityDocUrl)) {
            throw new BadRequestException("User must provide identity Documents before upgrading kyc level");
        }
        foundUser.setIdentityDocUrl(identityDocUrl);
        foundUser.setKyc(kyc);
        foundUser = userRepository.save(foundUser);
        return modelMapper.map(foundUser, UserDto.class);
    }
}
