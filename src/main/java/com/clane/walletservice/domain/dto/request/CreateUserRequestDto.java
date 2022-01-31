package com.clane.walletservice.domain.dto.request;


import com.clane.walletservice.utils.validations.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequestDto implements Serializable {

    @NotBlank(message = "first name must be provided")
    private String firstName;
    @NotBlank(message = "last name must be provided")
    private String lastName;
    @NotBlank(message = "email address must be provided")
    @Email
    private String emailAddress;
    @NotBlank(message = "phone number must be provided")
    @PhoneNumber
    private String phoneNumber;
    @NotBlank(message = "House address must be provided")
    private String houseAddress;

}
