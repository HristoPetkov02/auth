package com.tinqinacademy.auth.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.auth.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.auth.api.model.ErrorWrapper;
import com.tinqinacademy.auth.api.operations.login.LoginInput;
import com.tinqinacademy.auth.api.operations.login.LoginOperation;
import com.tinqinacademy.auth.api.operations.login.LoginOutput;
import com.tinqinacademy.auth.core.base.BaseOperationProcessor;

import com.tinqinacademy.auth.core.security.JwtTokenProvider;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginOperationProcessor extends BaseOperationProcessor<LoginInput, LoginOutput> implements LoginOperation {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Either<ErrorWrapper, LoginOutput> process(LoginInput input) {
        return Try.of(() -> login(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }


    private String jwtToken(LoginInput input) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword())
        );

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }


    private LoginOutput login(LoginInput input) {
        logStart(input);

        String jwt = jwtToken(input);
        LoginOutput output = LoginOutput.builder()
                .token(jwt)
                .build();
        logEnd(output);
        return output;
    }
}
