package com.clane.walletservice.service;

import com.clane.walletservice.domain.dto.UserDto;
import com.clane.walletservice.domain.dto.request.CreateUserRequestDto;
import com.clane.walletservice.domain.dto.request.UpgradeKycRequestDto;
import com.clane.walletservice.domain.dto.response.AppResponse;
import com.clane.walletservice.model.User;

public interface UserService {

    AppResponse<UserDto> createNewUser(CreateUserRequestDto createUserRequestDto);

    AppResponse<UserDto> confirmEmailAddress(String emailAddress);

    AppResponse<UserDto> upgradeUserKyc(UpgradeKycRequestDto upgradeKycRequestDto, String emailAddress);

    User findUserByEmail(String emailAddress);
}
