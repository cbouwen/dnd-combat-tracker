package com.example.dnd_combat_tracker.application.commands;

public record SetInitiativeCommand(
        String combatantId,
        int initiativeValue
) {
}
