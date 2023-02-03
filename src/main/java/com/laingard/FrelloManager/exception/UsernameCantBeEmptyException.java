package com.laingard.FrelloManager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsernameCantBeEmptyException extends ResponseStatusException {
    public UsernameCantBeEmptyException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}