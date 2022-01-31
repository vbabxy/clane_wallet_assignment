package com.clane.walletservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Request already exist")
public class ModelAlreadyExistException extends RuntimeException {


    public ModelAlreadyExistException(String message) {
        super(message);
    }
}
