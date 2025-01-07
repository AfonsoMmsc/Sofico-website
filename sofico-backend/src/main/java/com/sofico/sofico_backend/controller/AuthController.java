package com.sofico.sofico_backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sofico.sofico_backend.models.AuthRequest;
import com.sofico.sofico_backend.util.InvalidCredentialsException;
import com.sofico.sofico_backend.util.JWTUtil;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody AuthRequest authRequest) throws InvalidCredentialsException {
        try {
            logger.info("Authenticating user: {}", authRequest.getUsername());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            String token = jwtUtil.generateToken(authRequest.getUsername());
            logger.info("Authentication successful for user: {}", authRequest.getUsername());
            return token;
        } catch (AuthenticationException e) {
            String errorMessage = "Authentication failed for user: " + authRequest.getUsername();
            logger.error(errorMessage + " - Possible invalid credentials.", e);
            throw new InvalidCredentialsException(errorMessage, e);
        }
    }
}
