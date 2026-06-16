package com.example.dnd_combat_tracker.application.exceptions;

//TODO Delete this and all its usages
public class ActiveEncounterAlreadyExistsException extends RuntimeException {
    public ActiveEncounterAlreadyExistsException(String message) {
        super(message);
    }
}
