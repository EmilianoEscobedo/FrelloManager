package com.laingard.FrelloManager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class StatusProblemException extends ResponseStatusException {
    public StatusProblemException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
