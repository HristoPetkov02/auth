package com.tinqinacademy.auth.api.operations.validatejwt;

import com.tinqinacademy.auth.api.base.OperationInput;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidateJwtInput implements OperationInput {
    @NotNull(message = "jwt is required")
    private String jwt;
}
