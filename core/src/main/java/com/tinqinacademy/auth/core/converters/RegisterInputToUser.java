package com.tinqinacademy.auth.core.converters;

import com.tinqinacademy.auth.api.operations.register.RegisterInput;
import com.tinqinacademy.auth.core.base.BaseConverter;
import com.tinqinacademy.auth.persistence.models.User;
import com.tinqinacademy.auth.persistence.models.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RegisterInputToUser extends BaseConverter<RegisterInput, User> {
    private final PasswordEncoder passwordEncoder;
    @Override
    protected User convertObject(RegisterInput input) {
        User output = User.builder()
                .birthDate(input.getBirthDate())
                .email(input.getEmail())
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .password(passwordEncoder.encode(input.getPassword()))
                .username(input.getUsername())
                .phoneNumber(input.getPhoneNumber())
                .role(Role.USER)
                .build();
        return output;
    }
}
