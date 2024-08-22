package com.tinqinacademy.auth.api.operations.register;

import com.tinqinacademy.auth.api.base.OperationInput;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterInput implements OperationInput {
    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "First name is required")
    private String firstName;

    @NotNull(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;
}
