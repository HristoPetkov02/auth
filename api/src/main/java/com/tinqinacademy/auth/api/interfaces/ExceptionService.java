package com.tinqinacademy.auth.api.interfaces;



import com.tinqinacademy.auth.api.model.ErrorWrapper;
import org.springframework.web.bind.MethodArgumentNotValidException;

public interface ExceptionService {
    ErrorWrapper handleException(MethodArgumentNotValidException ex);
}
