package com.clane.walletservice.exception;


import com.clane.walletservice.domain.dto.response.AppResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;




@Slf4j
@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(ModelNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<?> handleUserNotFoundException(final ModelNotFoundException ex) {
        log.error("Not found exception thrown");

        AppResponse<Object> response= AppResponse.builder()
                .data("")
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .error(ex.getMessage())
                .build();

        return new ResponseEntity<>(response,  HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<?> handleBadRequestException(final BadRequestException ex) {
        log.error("Bad request exception thrown");

        AppResponse<Object> response = AppResponse.builder()
                .data("")
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .error(ex.getMessage())
                .build();

        return new ResponseEntity<>(response,  HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<?> handleThrowable(final Throwable ex) {
        log.error("Unexpected Error", ex);

        AppResponse<Object> response = AppResponse.builder()
                .data("")
                .status(500)
                .message("An unexpected internal server error occurred")
                .build();

        return new ResponseEntity<>(response,  HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(ModelAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ResponseEntity<?> handleModelAlreadyExistException(final ModelAlreadyExistException ex) {
        log.error("Model Already Exist exception thrown");

        AppResponse<Object> response = AppResponse.builder()
                .data("")
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response,  HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<?> processValidationError(MethodArgumentNotValidException ex) {

        BindingResult result = ex.getBindingResult();

        FieldError error = result.getFieldError();

        AppResponse<Object>response;

        if(error != null) {
            response = processFieldError(error);

        } else {
            response = AppResponse.builder()
                    .error("One or more Parameters are invalid")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("One or more Parameters are invalid")
                    .build();

        }
        return new ResponseEntity<>(response,  HttpStatus.BAD_REQUEST);
    }

    private AppResponse<Object> processFieldError(FieldError error) {

        return AppResponse.builder()
                .error(error.getDefaultMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(error.getDefaultMessage())
                .build();
    }

}
