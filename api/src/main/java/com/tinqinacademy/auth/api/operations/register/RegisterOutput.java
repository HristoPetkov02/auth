package com.tinqinacademy.auth.api.operations.register;

import com.tinqinacademy.auth.api.base.OperationOutput;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterOutput implements OperationOutput {
    private String id;
}
