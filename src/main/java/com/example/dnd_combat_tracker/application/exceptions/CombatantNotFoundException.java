package com.example.dnd_combat_tracker.application.exceptions;

public class CombatantNotFoundException extends RuntimeException {
    public CombatantNotFoundException(String message) {
        super(message);
    }
}
