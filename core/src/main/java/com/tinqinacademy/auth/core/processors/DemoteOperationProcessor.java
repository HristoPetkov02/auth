package com.tinqinacademy.auth.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.auth.api.exceptions.AuthApiException;
import com.tinqinacademy.auth.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.auth.api.model.ErrorWrapper;
import com.tinqinacademy.auth.api.operations.demote.DemoteInput;
import com.tinqinacademy.auth.api.operations.demote.DemoteOperation;
import com.tinqinacademy.auth.api.operations.demote.DemoteOutput;
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
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class DemoteOperationProcessor extends BaseOperationProcessor<DemoteInput, DemoteOutput> implements DemoteOperation {
    private final UserRepository userRepository;

    public DemoteOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, UserRepository userRepository) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.userRepository = userRepository;
    }

    @Override
    public Either<ErrorWrapper, DemoteOutput> process(DemoteInput input) {
        return Try.of(() -> demote(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }

    private User getUser(DemoteInput input) {
        User user = userRepository.findById(UUID.fromString(input.getUserId()))
                .orElseThrow(() -> new AuthApiException("User not found", HttpStatus.NOT_FOUND));
        if (user.getRole().name().equals("USER")) {
            throw new AuthApiException("User is already a user", HttpStatus.BAD_REQUEST);
        }
        return user;
    }

    private DemoteOutput demote(DemoteInput input) {
        logStart(input);
        validateInput(input);

        User user = getUser(input);

        user.setRole(Role.USER);
        userRepository.save(user);

        DemoteOutput output = DemoteOutput.builder().build();
        logEnd(output);
        return output;
    }
}
