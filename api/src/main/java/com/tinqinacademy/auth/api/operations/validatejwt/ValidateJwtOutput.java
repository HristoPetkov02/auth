package com.tinqinacademy.auth.api.operations.validatejwt;

import com.tinqinacademy.auth.api.base.OperationOutput;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidateJwtOutput implements OperationOutput {
    private Boolean isValid;
}
