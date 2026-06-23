package com.example.dnd_combat_tracker.application.usecases;

import com.example.dnd_combat_tracker.application.exceptions.EncounterNotFoundException;
import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import org.springframework.stereotype.Component;

@Component
public class DealDamageUseCase {

    private final EncounterRepositoryPort encounterRepositoryPort;

    public DealDamageUseCase(EncounterRepositoryPort encounterRepositoryPort) {
        this.encounterRepositoryPort = encounterRepositoryPort;
    }

    public CombatEncounter execute(String combatEncounterId, String combatantId, int damage) {
        CombatEncounter combatEncounter = encounterRepositoryPort.findById(combatEncounterId).orElseThrow(() -> new EncounterNotFoundException(combatEncounterId));
        combatEncounter.damageCombatant(combatantId, damage);
        encounterRepositoryPort.save(combatEncounter);
        return combatEncounter;
    }
}
