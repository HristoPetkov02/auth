package com.tinqinacademy.auth.api.operations.login;

import com.tinqinacademy.auth.api.base.OperationOutput;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginOutput implements OperationOutput {
    private String token;
}
