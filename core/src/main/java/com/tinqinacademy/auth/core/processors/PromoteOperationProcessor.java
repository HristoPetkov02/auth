package com.tinqinacademy.auth.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.auth.api.exceptions.AuthApiException;
import com.tinqinacademy.auth.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.auth.api.model.ErrorWrapper;
import com.tinqinacademy.auth.api.operations.promote.PromoteInput;
import com.tinqinacademy.auth.api.operations.promote.PromoteOperation;
import com.tinqinacademy.auth.api.operations.promote.PromoteOutput;
import com.tinqinacademy.auth.core.base.BaseOperationProcessor;
import com.tinqinacademy.auth.persistence.models.User;
import com.tinqinacademy.auth.persistence.models.enums.Role;
import com.tinqinacademy.auth.persistence.repository.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PromoteOperationProcessor extends BaseOperationProcessor<PromoteInput, PromoteOutput> implements PromoteOperation {
    private final UserRepository userRepository;

    public PromoteOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, UserRepository userRepository) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.userRepository = userRepository;
    }

    @Override
    public Either<ErrorWrapper, PromoteOutput> process(PromoteInput input) {
        return Try.of(() -> promote(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }

    private User getUser(PromoteInput input){
        User user = userRepository.findById(UUID.fromString(input.getUserId()))
                .orElseThrow(() -> new AuthApiException("User not found", HttpStatus.NOT_FOUND));
        if (user.getRole().name().equals("ADMIN")) {
            throw new AuthApiException("User is already an admin", HttpStatus.BAD_REQUEST);
        }
        return user;
    }

    private PromoteOutput promote(PromoteInput input) {
        logStart(input);
        validateInput(input);

        User user = getUser(input);

        user.setRole(Role.ADMIN);
        userRepository.save(user);

        PromoteOutput output = PromoteOutput.builder().build();
        logEnd(output);
        return output;
    }
}
