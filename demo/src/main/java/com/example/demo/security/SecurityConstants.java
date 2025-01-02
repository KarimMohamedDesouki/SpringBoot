package com.example.demo.security;

import org.springframework.core.env.Environment;

import com.example.demo.SpringApplicationContext;

public class SecurityConstants {
    // this is the data for the jwt token that will be used to authenticate the user
    public static final long EXPIRATION_TIME = 864000000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String TOKEN_SECRET = "a2s6das5d6as6d5as5d9a5s9da56sd295asd6a2sd65a9sd9a5sd6sa26sd59a5sd9a6sd26a5s9d9as5d6a2s6d59sads23";

    public static String getTokenSecret() {
        Environment environment = (Environment) SpringApplicationContext.getBean("environment");
        return environment.getProperty("tokenSecret");
    }
}
