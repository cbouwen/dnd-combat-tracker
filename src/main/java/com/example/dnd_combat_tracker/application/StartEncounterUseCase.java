package com.example.dnd_combat_tracker.application;

import com.example.dnd_combat_tracker.application.exceptions.EncounterNotFoundException;
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

    public void execute(Map<String, Integer> playerInitiatives) {
        CombatEncounter combatEncounter = this.encounterRepositoryPort.getActive().orElseThrow(() -> new EncounterNotFoundException("No active encounter"));
        playerInitiatives.forEach((combatantId, initiative) -> {
            combatEncounter.findCombatantById(combatantId)
                    .ifPresent(combatant -> combatant.setInitiative(initiative));
        });
        List<Combatant> npcs = combatEncounter.getCombatants().stream().filter(c -> c.getType() != CombatantType.PC).toList();
        npcs.forEach(c -> c.setInitiative(rollD20()));
        combatEncounter.startEncounter();
        this.encounterRepositoryPort.save(combatEncounter);
    }

    private static int rollD20() {
        return ThreadLocalRandom.current().nextInt(1, 21);
    }
}
