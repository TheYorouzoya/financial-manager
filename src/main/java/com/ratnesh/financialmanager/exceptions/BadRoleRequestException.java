package com.ratnesh.financialmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRoleRequestException extends RuntimeException {
    public BadRoleRequestException(String message) {
        super(message);
    }

    public BadRoleRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
