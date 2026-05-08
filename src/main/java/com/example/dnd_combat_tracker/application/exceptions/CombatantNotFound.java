package com.example.dnd_combat_tracker.application.exceptions;

public class CombatantNotFound extends RuntimeException {
    public CombatantNotFound(String message) {
        super(message);
    }
}
