package com.tinqinacademy.auth.rest.interceptor;

import com.tinqinacademy.auth.api.restroutes.RestApiRoutes;
import com.tinqinacademy.auth.core.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AccessInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    private final String[] adminRoutes = {RestApiRoutes.API_AUTH_PROMOTE, RestApiRoutes.API_AUTH_DEMOTE};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws IOException {
        if (isRouteAdminRoute(request.getRequestURI())) {
            final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized.");
                return false;
            }

            String jwt = authorizationHeader.substring(7);
            if (!jwtTokenProvider.validateToken(jwt)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized.");
                return false;
            }

            if (!jwtTokenProvider.extractRole(jwt).equals("ADMIN")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Forbidden.");
                return false;
            }
        }
        
        return true;
    }

    private boolean isRouteAdminRoute(String uri) {
        for (String route : adminRoutes) {
            if (uri.equals(route)) {
                return true;
            }
        }
        return false;
    }
}
