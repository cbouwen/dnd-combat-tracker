package com.example.dnd_combat_tracker.domain.exceptions;

public class NotAllInitiativesSetException extends RuntimeException {
    public NotAllInitiativesSetException(String message) {
        super(message);
    }
}
