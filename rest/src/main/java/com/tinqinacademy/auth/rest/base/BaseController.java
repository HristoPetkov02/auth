package com.tinqinacademy.auth.rest.base;


import com.tinqinacademy.auth.api.base.OperationOutput;
import com.tinqinacademy.auth.api.model.ErrorWrapper;
import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
    protected<O extends OperationOutput> ResponseEntity<?> handleWithCode(Either<ErrorWrapper,O> result, HttpStatus status){
        return result.isLeft() ? error(result) : new ResponseEntity<>(result.get(), status);
    }

    protected<O extends OperationOutput> ResponseEntity<?> handle(Either<ErrorWrapper,O> result){
        return result.isLeft() ? error(result) : new ResponseEntity<>(result.get(), HttpStatus.OK);
    }

    private<O extends OperationOutput> ResponseEntity<?> error(Either<ErrorWrapper,O> result){
        return new ResponseEntity<>(result.getLeft().getErrors(), result.getLeft().getErrorCode());
    }
}
