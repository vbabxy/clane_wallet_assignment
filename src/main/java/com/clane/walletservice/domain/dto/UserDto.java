package com.clane.walletservice.domain.dto;

import com.clane.walletservice.model.Kyc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {

    private long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String emailAddress;

    private String houseAddress;

    private boolean isEmailConfirmed;

    private String bvn;

    private String identityDocUrl;
}
