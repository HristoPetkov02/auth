package com.tinqinacademy.auth.core.converters;


import com.tinqinacademy.auth.api.operations.logout.LogoutInput;
import com.tinqinacademy.auth.core.base.BaseConverter;
import com.tinqinacademy.auth.core.security.JwtDecoder;
import com.tinqinacademy.auth.persistence.models.BlacklistedToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutInputToBlacklistedToken extends BaseConverter<LogoutInput, BlacklistedToken>{

    @Override
    protected BlacklistedToken convertObject(LogoutInput input) {
        String token = input.getToken().substring(7);
        BlacklistedToken blacklistedToken = BlacklistedToken.builder()
                .token(token)
                .build();
        return blacklistedToken;
    }
}
