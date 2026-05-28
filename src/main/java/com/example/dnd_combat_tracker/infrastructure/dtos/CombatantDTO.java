package com.example.dnd_combat_tracker.infrastructure.dtos;

public record CombatantDTO(
        String id,
        String name,
        String type,
        int currentHp,
        int maxHp,
        int ac,
        Integer initiative,
        int initiativeModifier
) {
}
