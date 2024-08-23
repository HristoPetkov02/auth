package com.tinqinacademy.auth.api.operations.recoverpassword;


import com.tinqinacademy.auth.api.base.OperationInput;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecoverPasswordInput implements OperationInput {
    @NotBlank(message = "email is required")
    @Email(message = "email is not valid")
    private String email;
}
