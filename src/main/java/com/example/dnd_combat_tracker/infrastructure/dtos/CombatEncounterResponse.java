package com.example.dnd_combat_tracker.infrastructure.dtos;

import com.example.dnd_combat_tracker.domain.CombatEncounter;

import java.util.List;

public record CombatEncounterResponse(
        String id,
        String encounterState,
        List<CombatantResponse> combatants
) {
    public static CombatEncounterResponse from(CombatEncounter encounter) {
        return new CombatEncounterResponse(
                encounter.getId(),
                encounter.getState().toString(),
                encounter.getCombatants().stream()
                        .map(CombatantResponse::from)
                        .toList()
        );
    }
}
