package com.example.dnd_combat_tracker.application.usecases;

import com.example.dnd_combat_tracker.application.exceptions.EncounterNotFoundException;
import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import org.springframework.stereotype.Component;

@Component
public class PreviousTurnUseCase {

    private final EncounterRepositoryPort encounterRepositoryPort;

    public PreviousTurnUseCase(EncounterRepositoryPort encounterRepositoryPort) {
        this.encounterRepositoryPort = encounterRepositoryPort;
    }

    public CombatEncounter execute(String encounterId) {
        CombatEncounter encounter = encounterRepositoryPort.findById(encounterId).orElseThrow(() -> new EncounterNotFoundException(encounterId));
        encounter.previousTurn();
        encounterRepositoryPort.save(encounter);
        return encounter;
    }
}
