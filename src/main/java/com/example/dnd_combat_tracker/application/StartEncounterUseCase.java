package com.example.dnd_combat_tracker.application;

import com.example.dnd_combat_tracker.application.exceptions.CombatantNotFoundException;
import com.example.dnd_combat_tracker.application.exceptions.EncounterNotFoundException;
import com.example.dnd_combat_tracker.application.exceptions.NotAllInitiativesSetException;
import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import com.example.dnd_combat_tracker.domain.Combatant;
import com.example.dnd_combat_tracker.domain.CombatantType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class StartEncounterUseCase {

    private final EncounterRepositoryPort encounterRepositoryPort;

    public StartEncounterUseCase(EncounterRepositoryPort encounterRepositoryPort) {
        this.encounterRepositoryPort = encounterRepositoryPort;
    }

    //TODO for this ticket: Find Encounter by encounterId
    public void execute(Map<String, Integer> playerInitiatives) {
        CombatEncounter combatEncounter = this.encounterRepositoryPort.getActive().orElseThrow(() -> new EncounterNotFoundException("No active encounter"));

        validatePCsIncluded(playerInitiatives, combatEncounter);
        validateAndSetPCInitiative(playerInitiatives, combatEncounter);
        setNPCInitiative(combatEncounter);

        combatEncounter.startEncounter();
        this.encounterRepositoryPort.save(combatEncounter);
    }

    private static void validatePCsIncluded(Map<String, Integer> playerInitiatives, CombatEncounter combatEncounter) {
        List<Combatant> pcs = combatEncounter.getCombatants().stream()
                .filter(c -> c.getType() == CombatantType.PC)
                .toList();
        pcs.forEach(pc -> {
            if (!playerInitiatives.containsKey(pc.getId())) {
                throw new NotAllInitiativesSetException("Missing initiative for PC: " + pc.getName());
            }
        });
    }

    private static void validateAndSetPCInitiative(Map<String, Integer> playerInitiatives, CombatEncounter combatEncounter) {
        playerInitiatives.forEach((combatantId, initiative) -> {
            Combatant combatant = combatEncounter.findCombatantById(combatantId)
                    .orElseThrow(() -> new CombatantNotFoundException("No combatant found for id: " + combatantId));
            if (initiative == null) {
                throw new NotAllInitiativesSetException("Initiative cannot be null for combatant: " + combatantId);
            }
            combatant.setInitiative(initiative);
        });
    }

    private static void setNPCInitiative(CombatEncounter combatEncounter) {
        List<Combatant> npcs = combatEncounter.getCombatants().stream().filter(c -> c.getType() != CombatantType.PC).toList();
        npcs.forEach(c -> c.setInitiative(rollD20()));
    }

    private static int rollD20() {
        return ThreadLocalRandom.current().nextInt(1, 21);
    }
}
