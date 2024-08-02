package com.tinqinacademy.auth.rest.globalexception;



import com.tinqinacademy.auth.api.model.ErrorWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex){
        String response = ex.getMessage();
        if (ex.getCause()!=null)
            response+="\nCause: "+ex.getCause();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
}

