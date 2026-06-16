package com.example.dnd_combat_tracker.application.usecases;

import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateEncounterUseCase {

    private final EncounterRepositoryPort encounterRepositoryPort;

    public CreateEncounterUseCase(EncounterRepositoryPort encounterRepositoryPort) {
        this.encounterRepositoryPort = encounterRepositoryPort;
    }

    public CombatEncounter execute() {
        CombatEncounter combatEncounter = CombatEncounter.create(UUID.randomUUID().toString());
        encounterRepositoryPort.save(combatEncounter);
        return combatEncounter;
    }

}
