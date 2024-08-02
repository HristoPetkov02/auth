package com.tinqinacademy.auth.core.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthApiException extends RuntimeException{

    private final HttpStatus httpStatus;

    public AuthApiException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }
}
