package com.example.dnd_combat_tracker.application.usecases;

import com.example.dnd_combat_tracker.application.exceptions.EncounterNotFoundException;
import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import org.springframework.stereotype.Component;

@Component
public class RemoveCombatantUseCase {

    private final EncounterRepositoryPort encounterRepositoryPort;

    public RemoveCombatantUseCase(EncounterRepositoryPort encounterRepositoryPort) {
        this.encounterRepositoryPort = encounterRepositoryPort;
    }

    public CombatEncounter execute(String combatEncounterId, String combatantId) {
        CombatEncounter combatEncounter = encounterRepositoryPort.findById(combatEncounterId).orElseThrow(() -> new EncounterNotFoundException(combatEncounterId));
        combatEncounter.removeCombatantById(combatantId);
        encounterRepositoryPort.save(combatEncounter);
        return combatEncounter;
    }

}
