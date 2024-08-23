package com.tinqinacademy.auth.api.operations.changepassword;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.auth.api.base.OperationInput;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChangePasswordInput implements OperationInput {
    @NotBlank(message = "Old password is required")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    private String newPassword;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @JsonIgnore
    private String token;
}
