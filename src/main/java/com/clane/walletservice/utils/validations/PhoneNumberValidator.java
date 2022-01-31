package com.clane.walletservice.utils.validations;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@Component
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    private String regex = "[0-9]{11,13}";

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pat = Pattern.compile(regex);
        return pat.matcher(phoneNumber).matches();
    }
}
