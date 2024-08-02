package com.tinqinacademy.auth.api.model;


import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorWrapper {
    private List<Error> errors;
    private HttpStatus errorCode;
}
