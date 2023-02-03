package com.laingard.FrelloManager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsernameNotFoundException extends ResponseStatusException {
    public UsernameNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
