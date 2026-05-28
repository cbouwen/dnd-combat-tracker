package com.example.dnd_combat_tracker.domain.exceptions;

public class EncounterNotActiveException extends RuntimeException {
    public EncounterNotActiveException(String message) {
        super(message);
    }
}
