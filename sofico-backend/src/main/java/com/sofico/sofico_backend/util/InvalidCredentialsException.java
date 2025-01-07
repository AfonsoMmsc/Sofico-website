package com.sofico.sofico_backend.util;

import org.springframework.security.core.AuthenticationException;

public class InvalidCredentialsException extends AuthenticationException {
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
