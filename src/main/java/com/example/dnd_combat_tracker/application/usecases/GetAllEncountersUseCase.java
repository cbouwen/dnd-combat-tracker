package com.example.dnd_combat_tracker.application.usecases;

import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllEncountersUseCase {

    private final EncounterRepositoryPort encounterRepositoryPort;

    public GetAllEncountersUseCase(EncounterRepositoryPort encounterRepositoryPort) {
        this.encounterRepositoryPort = encounterRepositoryPort;
    }

    public List<CombatEncounter> execute() {
        return encounterRepositoryPort.findAll();
    }
}
