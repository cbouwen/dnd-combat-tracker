package com.example.dnd_combat_tracker.application.exceptions;

public class CombatantNotFoundException extends RuntimeException {
    public CombatantNotFoundException(String combatantId) {
        super("No combatant found for id: " + combatantId);
    }
}
