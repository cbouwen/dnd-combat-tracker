package com.example.dnd_combat_tracker.application.commands;

import com.example.dnd_combat_tracker.domain.CombatantType;

import java.util.Optional;

public record AddCombatantCommand(
        String encounterId,
        CombatantType type,
        String name,
        Optional<String> templateId,
        int maxHP,
        int ac,
        int initiativeModifier
) {
}
