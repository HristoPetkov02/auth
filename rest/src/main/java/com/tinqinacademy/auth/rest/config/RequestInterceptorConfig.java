package com.tinqinacademy.auth.rest.config;

import com.tinqinacademy.auth.api.restroutes.RestApiRoutes;
import com.tinqinacademy.auth.rest.interceptor.AccessInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class RequestInterceptorConfig implements WebMvcConfigurer {
    private final AccessInterceptor accessInterceptor;

    @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        registry.addInterceptor(accessInterceptor)
                .addPathPatterns(
                        RestApiRoutes.API_AUTH_PROMOTE,
                        RestApiRoutes.API_AUTH_DEMOTE,
                        RestApiRoutes.API_AUTH_LOGOUT);
    }
}
