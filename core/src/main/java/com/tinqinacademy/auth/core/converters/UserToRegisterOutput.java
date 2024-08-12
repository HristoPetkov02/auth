package com.tinqinacademy.auth.core.converters;

import com.tinqinacademy.auth.api.operations.register.RegisterOutput;
import com.tinqinacademy.auth.core.base.BaseConverter;
import com.tinqinacademy.auth.persistence.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserToRegisterOutput extends BaseConverter<User, RegisterOutput>{
    @Override
    protected RegisterOutput convertObject(User input) {
        RegisterOutput output = RegisterOutput.builder()
                .id(String.valueOf(input.getId()))
                .build();
        return output;
    }
}
