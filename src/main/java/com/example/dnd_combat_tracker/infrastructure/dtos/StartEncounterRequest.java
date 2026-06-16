package com.example.dnd_combat_tracker.infrastructure.dtos;

import com.example.dnd_combat_tracker.application.commands.StartEncounterCommand;

import java.util.Map;

public record StartEncounterRequest(
        Map<String, Integer> playerInitiatives
) {

    public StartEncounterCommand toCommand(String id) {
        return new StartEncounterCommand(id, playerInitiatives);
    }
}
