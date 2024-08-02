package com.tinqinacademy.auth.core.exceptions;

import com.tinqinacademy.auth.api.model.Error;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class AuthValidationException extends RuntimeException{
    private final List<Error> errors;
    private final HttpStatus status;

    public AuthValidationException(String message, List<Error> errors, HttpStatus status){
        super(message);
        this.errors = errors;
        this.status = status;
    }
}
