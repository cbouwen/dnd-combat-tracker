package com.example.dnd_combat_tracker.application;

import com.example.dnd_combat_tracker.domain.CombatEncounter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateEncounterUseCase {

    public CombatEncounter execute() {
        return CombatEncounter.create(UUID.randomUUID().toString());
    }

}
