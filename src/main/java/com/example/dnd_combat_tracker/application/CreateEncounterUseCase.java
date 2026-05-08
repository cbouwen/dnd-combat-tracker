package com.example.dnd_combat_tracker.application;

import com.example.dnd_combat_tracker.application.exceptions.ActiveEncounterAlreadyExistsException;
import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import com.example.dnd_combat_tracker.domain.Combatant;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateEncounterUseCase {

    private final EncounterRepositoryPort encounterRepositoryPort;

    public CreateEncounterUseCase(EncounterRepositoryPort encounterRepositoryPort) {
        this.encounterRepositoryPort = encounterRepositoryPort;
    }

    public CombatEncounter execute() {
        if (encounterRepositoryPort.getActive().isPresent()) {
            throw new ActiveEncounterAlreadyExistsException("An active encounter already exists. End it before creating a new one.");
        }
        CombatEncounter combatEncounter = CombatEncounter.create(UUID.randomUUID().toString());
        combatEncounter.addCombatant(Combatant.createPlayer(
                UUID.randomUUID().toString(),
                "Zeraack",
                20,
                14,
                2
        ));
        combatEncounter.addCombatant(Combatant.createPlayer(
                UUID.randomUUID().toString(),
                "Solid",
                30,
                18,
                1
        ));
        //TODO: Replace hardcoded data by querying a database
        encounterRepositoryPort.save(combatEncounter);
        return combatEncounter;
    }

}
