package com.example.dnd_combat_tracker.application;

import com.example.dnd_combat_tracker.application.commands.StartEncounterCommand;
import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.application.usecases.StartEncounterUseCase;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import com.example.dnd_combat_tracker.domain.Combatant;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class StartEncounterUseCaseTest {

    @Test
    void happyPath() {
        List<Combatant> combatants = List.of(
                Combatant.createPlayer("pc1", "Zeraack", 20, 15, 3),
                Combatant.createPlayer("pc2", "Solid", 30, 18, 1),
                Combatant.createEnemy("enemy1", "Bandit", "bandit-template", 12, 13, 2),
                Combatant.createEnemy("enemy2", "Bandit", "bandit-template", 12, 13, 2),
                Combatant.createEnemy("enemy3", "Bandit", "bandit-template", 12, 13, 2)
        );
        Map<String, Integer> playerInitiatives = Map.of(
                "pc1", 18,
                "pc2", 12
        );
        String combatEncounter = "encounter1";
        String campaignId = UUID.randomUUID().toString();
        CombatEncounter encounter = CombatEncounter.createWithCombatants(combatEncounter, combatants, campaignId);
        EncounterRepositoryPort encounterRepositoryPort = mock(EncounterRepositoryPort.class);
        when(encounterRepositoryPort.findById(combatEncounter)).thenReturn(Optional.of(encounter));
        StartEncounterCommand startEncounterCommand = new StartEncounterCommand(combatEncounter, playerInitiatives);

        new StartEncounterUseCase(encounterRepositoryPort).execute(startEncounterCommand);

        assertThat(encounter.findCombatantById("pc1"))
                .isPresent()
                .get()
                .extracting(Combatant::getInitiative)
                .isEqualTo(18);
        assertThat(encounter.findCombatantById("pc2"))
                .isPresent()
                .get()
                .extracting(Combatant::getInitiative)
                .isEqualTo(12);
        assertThat(encounter.findCombatantById("enemy1"))
                .isPresent()
                .get()
                .extracting(Combatant::getInitiative)
                .isNotNull();
        assertThat(encounter.getState()).isEqualTo(CombatEncounter.EncounterState.ACTIVE);
        verify(encounterRepositoryPort).save(encounter);
    }
}
