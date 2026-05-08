package com.example.dnd_combat_tracker.application;

import com.example.dnd_combat_tracker.application.commands.SetInitiativeCommand;
import com.example.dnd_combat_tracker.application.exceptions.CombatantNotFound;
import com.example.dnd_combat_tracker.application.exceptions.EncounterNotFoundException;
import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import org.springframework.stereotype.Component;

@Component
public class SetInitiativeUseCase {

    private final EncounterRepositoryPort encounterRepositoryPort;

    public SetInitiativeUseCase(EncounterRepositoryPort encounterRepositoryPort) {
        this.encounterRepositoryPort = encounterRepositoryPort;
    }

    public void execute(SetInitiativeCommand setInitiativeCommand) {
        CombatEncounter encounter = encounterRepositoryPort.getActive()
                .orElseThrow(() -> new EncounterNotFoundException("Encounter not found"));
        encounter.findCombatantById(setInitiativeCommand.combatantId())
                .orElseThrow(() -> new CombatantNotFound("No combatant found for id " + setInitiativeCommand.combatantId()))
                .setInitiative(setInitiativeCommand.initiativeValue());
        encounterRepositoryPort.save(encounter);
    }
}
