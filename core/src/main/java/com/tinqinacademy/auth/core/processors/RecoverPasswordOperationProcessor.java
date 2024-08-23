package com.tinqinacademy.auth.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.auth.api.exceptions.AuthApiException;
import com.tinqinacademy.auth.api.interfaces.EmailService;
import com.tinqinacademy.auth.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.auth.api.model.ErrorWrapper;
import com.tinqinacademy.auth.api.operations.recoverpassword.RecoverPasswordInput;
import com.tinqinacademy.auth.api.operations.recoverpassword.RecoverPasswordOperation;
import com.tinqinacademy.auth.api.operations.recoverpassword.RecoverPasswordOutput;
import com.tinqinacademy.auth.core.base.BaseOperationProcessor;
import com.tinqinacademy.auth.persistence.models.User;
import com.tinqinacademy.auth.persistence.repository.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RecoverPasswordOperationProcessor extends BaseOperationProcessor<RecoverPasswordInput, RecoverPasswordOutput> implements RecoverPasswordOperation {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public RecoverPasswordOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public Either<ErrorWrapper, RecoverPasswordOutput> process(RecoverPasswordInput input) {
        return Try.of(() -> recoverPassword(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }


    private String sendMail(RecoverPasswordInput input){
        //generate random 6 digit code
        Random rnd = new Random();
        Integer number = rnd.nextInt(999999999);
        String code = String.format("%09d", number);

        //send email
        emailService.sendEmail(input.getEmail(),
                "Recover Password",
                String.format("Your recover code is: %s \nYou can use the code to log in your account and change your password!", code));

        return code;
    }

    private void validateUserAndSendMail(RecoverPasswordInput input){
        userRepository.findUserByEmail(input.getEmail())
                .ifPresent(user -> {
                    User updatedUser = user.toBuilder()
                            .password(passwordEncoder.encode(sendMail(input)))
                            .build();
                    userRepository.save(updatedUser);
                });
    }

    private RecoverPasswordOutput recoverPassword(RecoverPasswordInput input){
        logStart(input);
        validateInput(input);

        validateUserAndSendMail(input);

        RecoverPasswordOutput output = RecoverPasswordOutput.builder().build();
        logEnd(output);
        return output;
    }
}
