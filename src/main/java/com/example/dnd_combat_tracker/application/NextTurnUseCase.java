package com.example.dnd_combat_tracker.application;

import com.example.dnd_combat_tracker.application.exceptions.EncounterNotFoundException;
import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import org.springframework.stereotype.Component;

@Component
public class NextTurnUseCase {

    private final EncounterRepositoryPort encounterRepositoryPort;

    public NextTurnUseCase(EncounterRepositoryPort encounterRepositoryPort) {
        this.encounterRepositoryPort = encounterRepositoryPort;
    }

    public void execute() {
        CombatEncounter encounter = encounterRepositoryPort.getActive().orElseThrow(() -> new EncounterNotFoundException("No active encounter"));
        encounter.nextTurn();
        encounterRepositoryPort.save(encounter);
    }
}
