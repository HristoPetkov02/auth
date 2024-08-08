package com.tinqinacademy.auth.rest.controllers;

import com.tinqinacademy.auth.api.model.ErrorWrapper;
import com.tinqinacademy.auth.api.operations.login.LoginInput;
import com.tinqinacademy.auth.api.operations.login.LoginOutput;
import com.tinqinacademy.auth.api.restroutes.RestApiRoutes;
import com.tinqinacademy.auth.core.processors.LoginOperationProcessor;
import com.tinqinacademy.auth.rest.base.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final LoginOperationProcessor loginOperationProcessor;

    @Operation(summary = "Login", description = "This endpoint is for logging in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in"),
            @ApiResponse(responseCode = "400", description = "Wrong credentials used"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping(RestApiRoutes.API_AUTH_LOGIN)
    ResponseEntity<?> login(@RequestBody LoginInput input) {
        Either<ErrorWrapper,LoginOutput> output = loginOperationProcessor.process(input);

        return handleWithJwt(output);
    }
}
