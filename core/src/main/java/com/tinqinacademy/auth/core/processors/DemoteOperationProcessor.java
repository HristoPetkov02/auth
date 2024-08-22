package com.tinqinacademy.auth.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.auth.api.exceptions.AuthApiException;
import com.tinqinacademy.auth.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.auth.api.model.ErrorWrapper;
import com.tinqinacademy.auth.api.operations.demote.DemoteInput;
import com.tinqinacademy.auth.api.operations.demote.DemoteOperation;
import com.tinqinacademy.auth.api.operations.demote.DemoteOutput;
import com.tinqinacademy.auth.core.base.BaseOperationProcessor;
import com.tinqinacademy.auth.core.security.JwtDecoder;
import com.tinqinacademy.auth.persistence.models.User;
import com.tinqinacademy.auth.persistence.models.enums.Role;
import com.tinqinacademy.auth.persistence.repository.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class DemoteOperationProcessor extends BaseOperationProcessor<DemoteInput, DemoteOutput> implements DemoteOperation {
    private final UserRepository userRepository;
    private final JwtDecoder jwtDecoder;

    public DemoteOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, UserRepository userRepository, JwtDecoder jwtDecoder) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.userRepository = userRepository;
        this.jwtDecoder = jwtDecoder;
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


    private void validateAmountOfAdmins() {
        if (userRepository.countByRole(Role.ADMIN) == 1) {
            throw new AuthApiException("There must be at least one admin", HttpStatus.BAD_REQUEST);
        }
    }



    private void checkIfUserDemotesHimself(DemoteInput input){
        try {
            String id = jwtDecoder.extractId(input.getJwt());
            User user = userRepository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new AuthApiException("User not found", HttpStatus.NOT_FOUND));
            if (user.getId().toString().equals(input.getUserId())) {
                throw new AuthApiException("User cannot demote himself", HttpStatus.BAD_REQUEST);
            }
        }
        catch (IOException e){
            throw new AuthApiException("Invalid token", HttpStatus.BAD_REQUEST);
        }

    }

    private DemoteOutput demote(DemoteInput input) {
        logStart(input);
        validateInput(input);

        validateAmountOfAdmins();
        checkIfUserDemotesHimself(input);
        User user = getUser(input);

        user.setRole(Role.USER);
        userRepository.save(user);

        DemoteOutput output = DemoteOutput.builder().build();
        logEnd(output);
        return output;
    }
}
