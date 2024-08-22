package com.tinqinacademy.auth.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.auth.api.exceptions.AuthApiException;
import com.tinqinacademy.auth.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.auth.api.model.ErrorWrapper;
import com.tinqinacademy.auth.api.operations.logout.LogoutInput;
import com.tinqinacademy.auth.api.operations.logout.LogoutOperation;
import com.tinqinacademy.auth.api.operations.logout.LogoutOutput;
import com.tinqinacademy.auth.core.base.BaseOperationProcessor;
import com.tinqinacademy.auth.persistence.models.BlacklistedToken;
import com.tinqinacademy.auth.persistence.repository.BlackListedTokenRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LogoutOperationProcessor extends BaseOperationProcessor<LogoutInput, LogoutOutput> implements LogoutOperation {
    private final BlackListedTokenRepository blackListedTokenRepository;

    public LogoutOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, BlackListedTokenRepository blackListedTokenRepository) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.blackListedTokenRepository = blackListedTokenRepository;
    }

    @Override
    public Either<ErrorWrapper, LogoutOutput> process(LogoutInput input) {
        return Try.of(() -> logout(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }

    private LogoutOutput logout(LogoutInput input) {
        logStart(input);
        validateInput(input);



        BlacklistedToken blacklistedToken = conversionService.convert(input, BlacklistedToken.class);
        if (blacklistedToken == null) {
            throw new AuthApiException("Token not found", HttpStatus.NOT_FOUND);
        }
        blackListedTokenRepository.save(blacklistedToken);

        LogoutOutput output = LogoutOutput.builder().build();
        logEnd(output);
        return output;
    }
}
