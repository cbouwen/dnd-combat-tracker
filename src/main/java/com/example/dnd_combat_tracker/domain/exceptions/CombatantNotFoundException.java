package com.example.dnd_combat_tracker.domain.exceptions;

public class CombatantNotFoundException extends RuntimeException {
    public CombatantNotFoundException(String combatantId) {
        super("No combatant found for id: " + combatantId);
    }
}
