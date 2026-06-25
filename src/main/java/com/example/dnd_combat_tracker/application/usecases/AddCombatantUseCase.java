package com.example.dnd_combat_tracker.application.usecases;

import com.example.dnd_combat_tracker.application.commands.AddCombatantCommand;
import com.example.dnd_combat_tracker.application.exceptions.EncounterNotFoundException;
import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import com.example.dnd_combat_tracker.domain.Combatant;
import org.springframework.stereotype.Component;

@Component
public class AddCombatantUseCase {

    private final EncounterRepositoryPort encounterRepositoryPort;

    public AddCombatantUseCase(EncounterRepositoryPort encounterRepositoryPort) {
        this.encounterRepositoryPort = encounterRepositoryPort;
    }

    public Combatant execute(AddCombatantCommand addCombatantCommand) {
        CombatEncounter encounter = encounterRepositoryPort.findById(addCombatantCommand.encounterId())
                .orElseThrow(() -> new EncounterNotFoundException(addCombatantCommand.encounterId()));
        Combatant combatant = Combatant.create(
                addCombatantCommand.type(),
                addCombatantCommand.name(),
                addCombatantCommand.templateId(),
                addCombatantCommand.maxHP(),
                addCombatantCommand.ac(),
                addCombatantCommand.initiativeModifier()
        );
        encounter.addCombatant(combatant);
        encounterRepositoryPort.save(encounter);
        return combatant;
    }
}
