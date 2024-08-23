package com.tinqinacademy.auth.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.auth.api.interfaces.EmailService;
import com.tinqinacademy.auth.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.auth.api.model.ErrorWrapper;
import com.tinqinacademy.auth.api.operations.register.RegisterInput;
import com.tinqinacademy.auth.api.operations.register.RegisterOperation;
import com.tinqinacademy.auth.api.operations.register.RegisterOutput;
import com.tinqinacademy.auth.core.base.BaseOperationProcessor;
import com.tinqinacademy.auth.api.exceptions.AuthApiException;
import com.tinqinacademy.auth.persistence.models.User;
import com.tinqinacademy.auth.persistence.models.UserVerification;
import com.tinqinacademy.auth.persistence.repository.UserRepository;
import com.tinqinacademy.auth.persistence.repository.UserVerificationRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class RegisterOperationProcessor extends BaseOperationProcessor<RegisterInput, RegisterOutput> implements RegisterOperation {
    private final UserRepository userRepository;
    private final UserVerificationRepository userVerificationRepository;
    private final EmailService emailService;

    public RegisterOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, UserRepository userRepository, UserVerificationRepository userVerificationRepository, EmailService emailService) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.userRepository = userRepository;
        this.userVerificationRepository = userVerificationRepository;
        this.emailService = emailService;
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

    private String sendMail(RegisterInput input){
        //generate random 6 digit code
        Random rnd = new Random();
        Integer number = rnd.nextInt(999999);
        String code = String.format("%06d", number);

        //send email
        emailService.sendEmail(input.getEmail(),
                "Verification code",
                String.format("Your verification code is: %s", code));

        return code;
    }

    private void saveUserVerification(User user, String code){
        UserVerification userVerification = UserVerification.builder()
                .user(user)
                .verificationCode(code)
                .build();

        userVerificationRepository.save(userVerification);
    }

    private RegisterOutput register(RegisterInput input) {
        logStart(input);
        validateInput(input);
        checkIfUserExists(input);

        String code = sendMail(input);
        User user = conversionService.convert(input, User.class);

        userRepository.save(user);
        saveUserVerification(user,code);

        RegisterOutput output = conversionService.convert(user, RegisterOutput.class);
        logEnd(output);
        return output;
    }
}
