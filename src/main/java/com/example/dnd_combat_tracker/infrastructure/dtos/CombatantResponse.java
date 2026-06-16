package com.example.dnd_combat_tracker.infrastructure.dtos;

import com.example.dnd_combat_tracker.domain.Combatant;

public record CombatantResponse(
        String id,
        String name,
        String type,
        int currentHp,
        int maxHp,
        int ac,
        Integer initiative,
        int initiativeModifier
) {
    public static CombatantResponse from(Combatant combatant) {
        return new CombatantResponse(
                combatant.getId(),
                combatant.getName(),
                combatant.getType().toString(),
                combatant.getCurrentHP(),
                combatant.getMaxHP(),
                combatant.getAc(),
                combatant.getInitiative(),
                combatant.getInitiativeModifier()
        );
    }
}
