package com.tinqinacademy.auth.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.auth.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.auth.api.model.ErrorWrapper;
import com.tinqinacademy.auth.api.operations.validatejwt.ValidateJwtInput;
import com.tinqinacademy.auth.api.operations.validatejwt.ValidateJwtOperation;
import com.tinqinacademy.auth.api.operations.validatejwt.ValidateJwtOutput;
import com.tinqinacademy.auth.core.base.BaseOperationProcessor;
import com.tinqinacademy.auth.core.security.JwtTokenProvider;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ValidateJwtOperationProcessor extends BaseOperationProcessor<ValidateJwtInput, ValidateJwtOutput> implements ValidateJwtOperation {
    private final JwtTokenProvider jwtTokenProvider;
    public ValidateJwtOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, JwtTokenProvider jwtTokenProvider) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Either<ErrorWrapper, ValidateJwtOutput> process(ValidateJwtInput input) {
        return Try.of(() -> validateJwt(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }


    private ValidateJwtOutput validateJwt(ValidateJwtInput input) {
        logStart(input);
        validateInput(input);

        String jwt = input.getJwt().substring(7);

        ValidateJwtOutput output = ValidateJwtOutput.builder()
                .isValid(jwtTokenProvider.validateToken(jwt))
                .build();

        logEnd(output);
        return output;
    }
}
