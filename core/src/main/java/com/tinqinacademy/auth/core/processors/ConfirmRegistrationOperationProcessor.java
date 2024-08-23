package com.tinqinacademy.auth.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.auth.api.exceptions.AuthApiException;
import com.tinqinacademy.auth.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.auth.api.model.ErrorWrapper;
import com.tinqinacademy.auth.api.operations.confirmregistration.ConfirmRegistrationInput;
import com.tinqinacademy.auth.api.operations.confirmregistration.ConfirmRegistrationOperation;
import com.tinqinacademy.auth.api.operations.confirmregistration.ConfirmRegistrationOutput;
import com.tinqinacademy.auth.core.base.BaseOperationProcessor;
import com.tinqinacademy.auth.persistence.models.UserVerification;
import com.tinqinacademy.auth.persistence.repository.UserVerificationRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ConfirmRegistrationOperationProcessor extends BaseOperationProcessor<ConfirmRegistrationInput, ConfirmRegistrationOutput> implements ConfirmRegistrationOperation {
    private final UserVerificationRepository userVerificationRepository;

    public ConfirmRegistrationOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator, UserVerificationRepository userVerificationRepository) {
        super(conversionService, mapper, errorHandlerService, validator);
        this.userVerificationRepository = userVerificationRepository;
    }

    @Override
    public Either<ErrorWrapper, ConfirmRegistrationOutput> process(ConfirmRegistrationInput input) {
        return Try.of(() -> confirmRegistration(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
    }

    private ConfirmRegistrationOutput confirmRegistration(ConfirmRegistrationInput input) {
        logStart(input);
        validateInput(input);

        UserVerification userVerification = userVerificationRepository.findUserVerificationByVerificationCode(input.getConfirmationCode())
                .orElseThrow(() -> new AuthApiException("Invalid confirmation code", HttpStatus.BAD_REQUEST));

        userVerificationRepository.delete(userVerification);


        ConfirmRegistrationOutput output = ConfirmRegistrationOutput.builder().build();
        logEnd(output);
        return output;
    }
}
