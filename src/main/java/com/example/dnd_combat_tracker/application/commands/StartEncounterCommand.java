package com.example.dnd_combat_tracker.application.commands;

import java.util.Map;

public record StartEncounterCommand(
        String encounterId,
        Map<String, Integer> playerInitiatives
) {
}
