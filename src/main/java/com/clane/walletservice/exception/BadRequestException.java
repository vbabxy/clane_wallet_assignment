package com.clane.walletservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "invalid email passed has request")
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }


}

