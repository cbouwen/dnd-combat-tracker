package com.example.dnd_combat_tracker.application.usecases;

import com.example.dnd_combat_tracker.application.commands.StartEncounterCommand;
import com.example.dnd_combat_tracker.application.exceptions.EncounterNotFoundException;
import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import org.springframework.stereotype.Component;

@Component
public class StartEncounterUseCase {

    private final EncounterRepositoryPort encounterRepositoryPort;

    public StartEncounterUseCase(EncounterRepositoryPort encounterRepositoryPort) {
        this.encounterRepositoryPort = encounterRepositoryPort;
    }

    public CombatEncounter execute(StartEncounterCommand startEncounterCommand) {
        CombatEncounter combatEncounter = this.encounterRepositoryPort.findById(startEncounterCommand.encounterId())
                .orElseThrow(() -> new EncounterNotFoundException(startEncounterCommand.encounterId()));

        combatEncounter.setInitiatives(startEncounterCommand.playerInitiatives());
        combatEncounter.startEncounter();
        this.encounterRepositoryPort.save(combatEncounter);
        return combatEncounter;
    }
}
