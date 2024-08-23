package com.tinqinacademy.auth.rest.controllers;

import com.tinqinacademy.auth.api.operations.changepassword.ChangePasswordInput;
import com.tinqinacademy.auth.api.operations.changepassword.ChangePasswordOperation;
import com.tinqinacademy.auth.api.operations.demote.DemoteInput;
import com.tinqinacademy.auth.api.operations.demote.DemoteOperation;
import com.tinqinacademy.auth.api.operations.login.LoginInput;
import com.tinqinacademy.auth.api.operations.login.LoginOperation;
import com.tinqinacademy.auth.api.operations.logout.LogoutInput;
import com.tinqinacademy.auth.api.operations.logout.LogoutOperation;
import com.tinqinacademy.auth.api.operations.promote.PromoteInput;
import com.tinqinacademy.auth.api.operations.promote.PromoteOperation;
import com.tinqinacademy.auth.api.operations.register.RegisterInput;
import com.tinqinacademy.auth.api.operations.register.RegisterOperation;
import com.tinqinacademy.auth.api.operations.validatejwt.ValidateJwtInput;
import com.tinqinacademy.auth.api.operations.validatejwt.ValidateJwtOperation;
import com.tinqinacademy.auth.api.restroutes.RestApiRoutes;
import com.tinqinacademy.auth.rest.base.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final LoginOperation loginOperation;
    private final RegisterOperation registerOperation;
    private final ValidateJwtOperation validateJwtOperation;
    private final PromoteOperation promoteOperation;
    private final DemoteOperation demoteOperation;
    private final LogoutOperation logoutOperation;
    private final ChangePasswordOperation changePasswordOperation;

    @Operation(summary = "Login", description = "This endpoint is for logging in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in"),
            @ApiResponse(responseCode = "400", description = "Wrong credentials used"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping(RestApiRoutes.API_AUTH_LOGIN)
    public ResponseEntity<?> login(@RequestBody LoginInput input) {
        return handleWithJwt(loginOperation.process(input));
    }

    @Operation(summary = "Register", description = "This endpoint is for registering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully registered"),
            @ApiResponse(responseCode = "400", description = "Username or email already exists")
    })
    @PostMapping(RestApiRoutes.API_AUTH_REGISTER)
    public ResponseEntity<?> register(@RequestBody RegisterInput input) {
        return handle(registerOperation.process(input));
    }


    @Operation(summary = "Validate JWT.",
            description = "Returns true/false whether it is valid or not.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jwt has been validated successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "404", description = "Not found.")
    })
    @PostMapping(RestApiRoutes.API_AUTH_CHECK_JWT)
    public ResponseEntity<?> validateJwt(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        ValidateJwtInput input = ValidateJwtInput.builder().jwt(authorizationHeader).build();
        return handle(validateJwtOperation.process(input));
    }


    @Operation(summary = "Promote user to admin", description = "This endpoint is for promoting a user to admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully promoted user to admin"),
            @ApiResponse(responseCode = "400", description = "User is already an admin"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping(RestApiRoutes.API_AUTH_PROMOTE)
    public ResponseEntity<?> promote(@RequestBody PromoteInput input) {
        return handle(promoteOperation.process(input));
    }


    @Operation(summary = "Demote user from admin", description = "This endpoint is for demoting a user from admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully demoted user from admin"),
            @ApiResponse(responseCode = "400", description = "User is not an admin"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping(RestApiRoutes.API_AUTH_DEMOTE)
    public ResponseEntity<?> demote(@RequestBody DemoteInput input, HttpServletRequest request) {
        DemoteInput updatedInput = DemoteInput.builder()
                .jwt(request.getHeader(HttpHeaders.AUTHORIZATION))
                .userId(input.getUserId())
                .build();
        return handle(demoteOperation.process(updatedInput));
    }


    @Operation(summary = "Logout", description = "This endpoint is for logging out")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged out"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping(RestApiRoutes.API_AUTH_LOGOUT)
    public ResponseEntity<?> logout(HttpServletRequest request) {
        LogoutInput input = LogoutInput.builder()
                .token(request.getHeader(HttpHeaders.AUTHORIZATION))
                .build();
        return handle(logoutOperation.process(input));
    }


    @Operation(summary = "Change password", description = "This endpoint is for changing password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully changed password"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping(RestApiRoutes.API_AUTH_CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordInput input, HttpServletRequest request) {
        ChangePasswordInput updatedInput = input.toBuilder()
                .token(request.getHeader(HttpHeaders.AUTHORIZATION))
                .build();
        return handle(changePasswordOperation.process(updatedInput));
    }
}
