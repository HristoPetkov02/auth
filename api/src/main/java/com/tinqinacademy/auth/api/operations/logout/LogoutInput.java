package com.tinqinacademy.auth.api.operations.logout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.auth.api.base.OperationInput;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogoutInput implements OperationInput {
    @JsonIgnore
    private String token;
}
