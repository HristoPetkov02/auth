package com.tinqinacademy.auth.rest.controllers;

import com.tinqinacademy.auth.api.model.ErrorWrapper;
import com.tinqinacademy.auth.api.operations.login.LoginInput;
import com.tinqinacademy.auth.api.operations.login.LoginOutput;
import com.tinqinacademy.auth.api.operations.register.RegisterInput;
import com.tinqinacademy.auth.api.operations.register.RegisterOutput;
import com.tinqinacademy.auth.api.operations.validatejwt.ValidateJwtInput;
import com.tinqinacademy.auth.api.restroutes.RestApiRoutes;
import com.tinqinacademy.auth.core.processors.LoginOperationProcessor;
import com.tinqinacademy.auth.core.processors.RegisterOperationProcessor;
import com.tinqinacademy.auth.core.processors.ValidateJwtOperationProcessor;
import com.tinqinacademy.auth.core.security.JwtTokenProvider;
import com.tinqinacademy.auth.rest.base.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.vavr.control.Either;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final LoginOperationProcessor loginOperationProcessor;
    private final RegisterOperationProcessor registerOperationProcessor;
    private final ValidateJwtOperationProcessor validateJwtOperationProcessor;

    @Operation(summary = "Login", description = "This endpoint is for logging in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in"),
            @ApiResponse(responseCode = "400", description = "Wrong credentials used"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping(RestApiRoutes.API_AUTH_LOGIN)
    public ResponseEntity<?> login(@RequestBody LoginInput input) {
        return handleWithJwt(loginOperationProcessor.process(input));
    }

    @Operation(summary = "Register", description = "This endpoint is for registering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully registered"),
            @ApiResponse(responseCode = "400", description = "Username or email already exists")
    })
    @PostMapping(RestApiRoutes.API_AUTH_REGISTER)
    public ResponseEntity<?> register(@RequestBody RegisterInput input) {
        return handle(registerOperationProcessor.process(input));
    }


    @Operation(summary = "Validate JWT.",
            description = "Returns true/false whether it is valid or not.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jwt has been validated successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "404", description = "Not found.")
    })
    @PostMapping(RestApiRoutes.API_AUTH_CHECK_JWT)
    public ResponseEntity<?> checkJwt(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        ValidateJwtInput input = ValidateJwtInput.builder().jwt(authorizationHeader).build();
        return handle(validateJwtOperationProcessor.process(input));
    }
}
