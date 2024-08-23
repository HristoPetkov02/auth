package com.tinqinacademy.auth.api.operations.confirmregistration;

import com.tinqinacademy.auth.api.base.OperationInput;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ConfirmRegistrationInput implements OperationInput {
    @NotBlank(message = "Confirmation code is required")
    private String confirmationCode;
}
