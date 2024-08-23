package com.tinqinacademy.auth.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.auth.api.exceptions.AuthApiException;
import com.tinqinacademy.auth.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.auth.api.model.ErrorWrapper;
import com.tinqinacademy.auth.api.operations.changepassword.ChangePasswordInput;
import com.tinqinacademy.auth.api.operations.changepassword.ChangePasswordOperation;
import com.tinqinacademy.auth.api.operations.changepassword.ChangePasswordOutput;
import com.tinqinacademy.auth.core.base.BaseOperationProcessor;
import com.tinqinacademy.auth.core.security.JwtDecoder;
import com.tinqinacademy.auth.persistence.models.User;
import com.tinqinacademy.auth.persistence.repository.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.SneakyThrows;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChangePasswordOperationProcessor extends BaseOperationProcessor<ChangePasswordInput, ChangePasswordOutput> implements ChangePasswordOperation {
    private final UserRepository userRepository;
    private final JwtDecoder jwtDecoder;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, UserRepository userRepository, JwtDecoder jwtDecoder, PasswordEncoder passwordEncoder) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.userRepository = userRepository;
        this.jwtDecoder = jwtDecoder;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Either<ErrorWrapper, ChangePasswordOutput> process(ChangePasswordInput input) {
        return Try.of(() -> changePassword(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }

    @SneakyThrows
    private User validateIfUserIsOwnerOfEmail(ChangePasswordInput input){
        String id = jwtDecoder.decodeJwt(input.getToken()).get("sub").toString();
        User user = userRepository.findById(UUID.fromString(id)).orElseThrow(
            () -> new AuthApiException("User not found", HttpStatus.NOT_FOUND)
        );
        if(!user.getEmail().equals(input.getEmail())){
            throw new AuthApiException("User not owner of email", HttpStatus.BAD_REQUEST);
        }
        return user;
    }


    private ChangePasswordOutput changePassword(ChangePasswordInput input){
        logStart(input);
        validateInput(input);

        User user = validateIfUserIsOwnerOfEmail(input);

        User updatedUser = user.toBuilder()
                .password(passwordEncoder.encode(input.getNewPassword()))
                .build();
        userRepository.save(updatedUser);

        ChangePasswordOutput output = ChangePasswordOutput.builder().build();
        logEnd(output);
        return output;
    }
}
