package com.tinqinacademy.auth.core.errorhandler;


import com.tinqinacademy.auth.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.auth.api.model.Error;
import com.tinqinacademy.auth.api.model.ErrorWrapper;
import com.tinqinacademy.auth.core.exceptions.AuthApiException;
import com.tinqinacademy.auth.core.exceptions.AuthValidationException;
import io.vavr.API;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Slf4j
@Component
public class ErrorHandlerServiceImpl implements ErrorHandlerService {
    @Override
    public ErrorWrapper handle(Throwable throwable) {
        return Match(throwable).of(
                API.Case(API.$(instanceOf(AuthApiException.class)), this::handleAuthApiException),
                API.Case(API.$(instanceOf(AuthValidationException.class)), this::handleAuthValidationException),
                Case($(), this::handleDefaultException)
        );
    }

    private ErrorWrapper handleAuthApiException(AuthApiException ex) {
        return ErrorWrapper.builder()
                .errorCode(ex.getHttpStatus())
                .errors(
                        List.of(
                                Error.builder()
                                        .message(ex.getMessage())
                                        .build()))
                .build();
    }

    private ErrorWrapper handleAuthValidationException(AuthValidationException ex) {
        return ErrorWrapper.builder()
                .errorCode(ex.getStatus())
                .errors(ex.getErrors())
                .build();
    }

    private ErrorWrapper handleDefaultException(Throwable ex) {
        return ErrorWrapper.builder()
                .errorCode(HttpStatus.BAD_REQUEST)
                .errors(
                        List.of(
                                Error.builder()
                                        .message(ex.getMessage())
                                        .build()))
                .build();
    }
}
