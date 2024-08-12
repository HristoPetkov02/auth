package com.tinqinacademy.auth.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.auth.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.auth.api.model.ErrorWrapper;
import com.tinqinacademy.auth.api.operations.login.LoginInput;
import com.tinqinacademy.auth.api.operations.login.LoginOperation;
import com.tinqinacademy.auth.api.operations.login.LoginOutput;
import com.tinqinacademy.auth.core.base.BaseOperationProcessor;

import com.tinqinacademy.auth.core.exceptions.AuthApiException;
import com.tinqinacademy.auth.core.security.JwtTokenProvider;
import com.tinqinacademy.auth.persistence.models.User;
import com.tinqinacademy.auth.persistence.repository.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginOperationProcessor extends BaseOperationProcessor<LoginInput, LoginOutput> implements LoginOperation {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Either<ErrorWrapper, LoginOutput> process(LoginInput input) {
        return Try.of(() -> login(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }


    private User checkCredentials(LoginInput input) {

        User user = userRepository.findUserByUsername(input.getUsername())
                .orElseThrow(() -> new AuthApiException("Invalid credentials", HttpStatus.BAD_REQUEST));
        if (!passwordEncoder.matches(input.getPassword(), user.getPassword())) {
            throw new AuthApiException("Invalid credentials", HttpStatus.BAD_REQUEST);
        }
        return user;
    }



    private LoginOutput login(LoginInput input) {
        logStart(input);

        validateInput(input);


        User user = checkCredentials(input);
        String jwt = jwtTokenProvider.generateToken(user);
        LoginOutput output = LoginOutput.builder()
                .token(jwt)
                .build();
        logEnd(output);
        return output;
    }
}
