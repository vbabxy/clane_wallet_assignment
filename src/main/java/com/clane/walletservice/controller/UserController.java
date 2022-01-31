package com.clane.walletservice.controller;

import com.clane.walletservice.domain.dto.request.CreateUserRequestDto;
import com.clane.walletservice.domain.dto.request.UpgradeKycRequestDto;
import com.clane.walletservice.domain.dto.response.AppResponse;
import com.clane.walletservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.clane.walletservice.controller.BaseApiController.USERS;

@RestController
@Slf4j
@Api(value = BaseApiController.BASE_API_PATH+ USERS, tags = "user-controller")
@RequestMapping(BaseApiController.BASE_API_PATH +USERS)
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ApiOperation(value = "Create new User",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = AppResponse.class)
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserRequestDto createUserRequestDto) {

        var response = userService.createNewUser(createUserRequestDto);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/confirmEmailAddress/{emailAddress}")
    @ApiOperation(value = "Confirm Email Address",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = AppResponse.class)
    public ResponseEntity<?> confirmEmail(@PathVariable("emailAddress") String emailAddress) {

        var response = userService.confirmEmailAddress(emailAddress);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/upgrade/user/{emailAddress}/kyc/")
    @ApiOperation(value = "Upgrade user Kyc",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = AppResponse.class)
    public ResponseEntity<?> upgradeUserKyc(@PathVariable("emailAddress") String emailAddress,
                                            @RequestBody UpgradeKycRequestDto upgradeKycRequestDto) {

        var response = userService.upgradeUserKyc(upgradeKycRequestDto,emailAddress);
        return ResponseEntity.ok().body(response);
    }
}
