package com.tinqinacademy.auth.restexport;

import com.tinqinacademy.auth.api.operations.validatejwt.ValidateJwtInput;
import com.tinqinacademy.auth.api.operations.validatejwt.ValidateJwtOutput;
import com.tinqinacademy.auth.api.restroutes.RestApiRoutes;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface AuthRestExport {
    @RequestLine("POST "+ RestApiRoutes.API_AUTH_CHECK_JWT)
    @Headers({"Authorization: {authorizationHeader}"})
    ValidateJwtOutput validateJwt(@Param("authorizationHeader") String authorizationHeader);

}
