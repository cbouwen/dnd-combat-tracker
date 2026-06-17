package com.example.dnd_combat_tracker.application.usecases;

import com.example.dnd_combat_tracker.application.exceptions.EncounterNotFoundException;
import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import org.springframework.stereotype.Component;

@Component
public class GetEncounterUseCase {
    private final EncounterRepositoryPort encounterRepositoryPort;

    public GetEncounterUseCase(EncounterRepositoryPort encounterRepositoryPort) {
        this.encounterRepositoryPort = encounterRepositoryPort;
    }

    public CombatEncounter execute(String encounterId) {
        return encounterRepositoryPort.findById(encounterId).orElseThrow(() -> new EncounterNotFoundException(encounterId));
    }
}
