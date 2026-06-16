package com.example.dnd_combat_tracker.infrastructure.dtos;

import com.example.dnd_combat_tracker.application.commands.AddCombatantCommand;
import com.example.dnd_combat_tracker.domain.CombatantType;

public record AddCombatantRequest(
        CombatantType type,
        String name,
        String templateId,
        int maxHP,
        int ac,
        int initiativeModifier
) {
    public AddCombatantCommand toCommand(String encounterId) {
        return new AddCombatantCommand(
                encounterId,
                type,
                name,
                templateId,
                maxHP,
                ac,
                initiativeModifier
        );
    }
}
