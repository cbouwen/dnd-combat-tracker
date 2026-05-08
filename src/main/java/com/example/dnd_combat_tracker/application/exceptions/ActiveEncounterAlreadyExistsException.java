package com.example.dnd_combat_tracker.application.exceptions;

public class ActiveEncounterAlreadyExistsException extends RuntimeException {
    public ActiveEncounterAlreadyExistsException(String message) {
        super(message);
    }
}
