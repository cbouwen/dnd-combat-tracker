package com.example.dnd_combat_tracker.application.commands;

import com.example.dnd_combat_tracker.domain.CombatantType;

public record AddCombatantCommand(
        String encounterId,
        CombatantType type,
        String name,
        String templateId,
        int maxHP,
        int ac,
        int initiativeModifier
) {
}
