package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.service.UserService;

@EnableWebSecurity
@Configuration
public class WebSecurity {

    private final UserService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    public WebSecurity(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userDetailsService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        // for customize login url path
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager);
        authenticationFilter.setFilterProcessesUrl("/users/login");

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll() // Allow POST
                                                                                                     // requests to
                                                                                                     // /users
                        .anyRequest().authenticated())
                .authenticationManager(authenticationManager)
                .addFilter(authenticationFilter)
                .addFilter(new AuthorizationFilter(authenticationManager))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }
}