package com.tinqinacademy.auth.core.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.auth.api.base.OperationInput;
import com.tinqinacademy.auth.api.base.OperationOutput;
import com.tinqinacademy.auth.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.auth.api.model.Error;
import com.tinqinacademy.auth.core.exceptions.AuthApiException;
import com.tinqinacademy.auth.core.exceptions.AuthValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseOperationProcessor<I extends OperationInput, O extends OperationOutput> {
    protected final ConversionService conversionService;
    protected final ObjectMapper mapper;
    protected final ErrorHandlerService errorHandlerService;
    protected final Validator validator;

    protected void validateInput(I input) {
        Set<ConstraintViolation<I>> violations = validator.validate(input);
        if (!violations.isEmpty()) {
            List<Error> errors = violations.stream()
                    .map(violation -> Error.builder()
                            .field(violation.getPropertyPath().toString())
                            .message(violation.getMessage())
                            .build()).collect(Collectors.toList());
            throw new AuthValidationException(
                    "Error validating input",
                    errors,
                    HttpStatus.BAD_REQUEST);
        }
    }

    protected void logStart(I input) {
        log.info(
                String.format("Start %s input = %s",
                        this.getClass().getSimpleName(),
                        input));
    }

    protected void logEnd(O output) {
        log.info(
                String.format("End %s output = %s",
                        this.getClass().getSimpleName(),
                        output));
    }


}
