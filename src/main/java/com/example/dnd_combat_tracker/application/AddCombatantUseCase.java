package com.example.dnd_combat_tracker.application;

import com.example.dnd_combat_tracker.application.commands.AddCombatantCommand;
import com.example.dnd_combat_tracker.application.exceptions.EncounterNotFoundException;
import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import com.example.dnd_combat_tracker.domain.Combatant;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AddCombatantUseCase {

    private final EncounterRepositoryPort encounterRepositoryPort;

    public AddCombatantUseCase(EncounterRepositoryPort encounterRepositoryPort) {
        this.encounterRepositoryPort = encounterRepositoryPort;
    }

    public Combatant execute(AddCombatantCommand addCombatantCommand) {
        CombatEncounter encounter = encounterRepositoryPort.findById(addCombatantCommand.encounterId())
                .orElseThrow(() -> new EncounterNotFoundException(addCombatantCommand.encounterId()));
        String combatantID = UUID.randomUUID().toString();
        Combatant combatant = switch (addCombatantCommand.type()) {
            case PC -> Combatant.createPlayer(
                    combatantID,
                    addCombatantCommand.name(),
                    addCombatantCommand.maxHP(),
                    addCombatantCommand.ac(),
                    addCombatantCommand.initiativeModifier()
            );
            case NPC -> Combatant.createNPC(
                    combatantID,
                    addCombatantCommand.name(),
                    addCombatantCommand.templateId(),
                    addCombatantCommand.maxHP(),
                    addCombatantCommand.ac(),
                    addCombatantCommand.initiativeModifier()
            );
            case ENEMY -> Combatant.createEnemy(
                    combatantID,
                    addCombatantCommand.name(),
                    addCombatantCommand.templateId(),
                    addCombatantCommand.maxHP(),
                    addCombatantCommand.ac(),
                    addCombatantCommand.initiativeModifier()
            );
        };
        encounter.addCombatant(combatant);
        encounterRepositoryPort.save(encounter);
        return combatant;
    }
}
