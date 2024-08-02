package com.tinqinacademy.auth.api.interfaces;


import com.tinqinacademy.auth.api.model.ErrorWrapper;

public interface ErrorHandlerService {
    ErrorWrapper handle(Throwable throwable);
}
