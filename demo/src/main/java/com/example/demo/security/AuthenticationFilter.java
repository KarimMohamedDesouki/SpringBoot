package com.example.demo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.SpringApplicationContext;
import com.example.demo.service.UserService;
import com.example.demo.shared.dto.UserDto;
import com.example.demo.ui.model.request.userLoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            userLoginRequest creds = new ObjectMapper().readValue(req.getInputStream(), userLoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException, ServletException {

        byte[] secretKeyBytes = Base64.getEncoder().encode(SecurityConstants.getTokenSecret().getBytes());
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
        Instant now = Instant.now();

        String userName = ((User) auth.getPrincipal()).getUsername();
        // create the token and add username to the header
        String token = Jwts.builder()
                .setSubject(userName)
                .setExpiration(
                        Date.from(now.plusMillis(SecurityConstants.EXPIRATION_TIME)))
                .setIssuedAt(Date.from(now)).signWith(secretKey, SignatureAlgorithm.HS512).compact();

        // here we will add what data we nedd to be added in the token so it can be used
        // in the front end
        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImplementation");
        UserDto userDto = userService.getUser(userName);
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        res.addHeader("userId", userDto.getUserId());
        res.addHeader("FullName", userDto.getFirstName() + " " + userDto.getLastName());
    }

}
