package com.example.dnd_combat_tracker.application.exceptions;

public class EncounterNotFoundException extends RuntimeException {
    public EncounterNotFoundException(String message) {
        super("Encounter not found for id: " + message);
    }
}
