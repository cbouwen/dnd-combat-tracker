package com.example.dnd_combat_tracker.infrastructure.dtos;

import java.util.List;

public record CombatEncounterDTO(
        String id,
        String encounterState,
        List<CombatantDTO> combatants
) {
}
