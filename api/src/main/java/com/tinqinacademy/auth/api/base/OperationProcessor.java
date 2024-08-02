package com.tinqinacademy.auth.api.base;


import com.tinqinacademy.auth.api.model.ErrorWrapper;
import io.vavr.control.Either;

public interface OperationProcessor <I extends OperationInput, O extends OperationOutput>{
    Either<ErrorWrapper,O> process(I input);
}
