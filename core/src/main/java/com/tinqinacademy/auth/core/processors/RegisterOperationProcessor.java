package com.tinqinacademy.auth.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.auth.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.auth.api.model.ErrorWrapper;
import com.tinqinacademy.auth.api.operations.register.RegisterInput;
import com.tinqinacademy.auth.api.operations.register.RegisterOperation;
import com.tinqinacademy.auth.api.operations.register.RegisterOutput;
import com.tinqinacademy.auth.core.base.BaseOperationProcessor;
import com.tinqinacademy.auth.api.exceptions.AuthApiException;
import com.tinqinacademy.auth.persistence.models.User;
import com.tinqinacademy.auth.persistence.repository.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegisterOperationProcessor extends BaseOperationProcessor<RegisterInput, RegisterOutput> implements RegisterOperation {
    private final UserRepository userRepository;
    public RegisterOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, UserRepository userRepository) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.userRepository = userRepository;
    }

    @Override
    public Either<ErrorWrapper, RegisterOutput> process(RegisterInput input) {
        return Try.of(() -> register(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }

    private void checkIfUserExists(RegisterInput input) {
        userRepository.findUserByUsername(input.getUsername()).ifPresent(user -> {
            throw new AuthApiException("Username already exists", HttpStatus.BAD_REQUEST);
        });

        userRepository.findUserByEmail(input.getEmail()).ifPresent(user -> {
            throw new AuthApiException("Email already exists", HttpStatus.BAD_REQUEST);
        });
    }

    private RegisterOutput register(RegisterInput input) {
        logStart(input);
        validateInput(input);
        checkIfUserExists(input);

        User user = conversionService.convert(input, User.class);

        userRepository.save(user);

        RegisterOutput output = conversionService.convert(user, RegisterOutput.class);
        logEnd(output);
        return output;
    }
}
