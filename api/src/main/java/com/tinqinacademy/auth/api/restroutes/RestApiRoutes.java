package com.tinqinacademy.auth.api.restroutes;

public class RestApiRoutes {
    public final static String API = "/api/v1";

    public final static String API_AUTH_LOGIN = API + "/auth/login";
    public final static String API_AUTH_REGISTER = API + "/auth/register";
    public final static String API_AUTH_CHECK_JWT = API + "/auth/validate-jwt";
    public final static String API_AUTH_PROMOTE = API + "/auth/promote";
    public final static String API_AUTH_DEMOTE = API + "/auth/demote";
    public final static String API_AUTH_LOGOUT = API + "/auth/logout";
    public final static String API_AUTH_CHANGE_PASSWORD = API + "/auth/change-password";
    public final static String API_AUTH_CONFIRM_REGISTRATION = API + "/auth/confirm-registration";
}
