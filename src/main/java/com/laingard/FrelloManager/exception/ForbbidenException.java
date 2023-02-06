package com.laingard.FrelloManager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ForbbidenException extends ResponseStatusException {
    public ForbbidenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
