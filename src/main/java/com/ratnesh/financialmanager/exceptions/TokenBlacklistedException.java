package com.ratnesh.financialmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenBlacklistedException extends AuthenticationException {
    public TokenBlacklistedException(String message) {
        super(message);
    }

    public TokenBlacklistedException(String message, Throwable cause) {
        super(message, cause);
    }
}
