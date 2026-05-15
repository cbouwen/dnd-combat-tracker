package com.example.dnd_combat_tracker.application;

import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import com.example.dnd_combat_tracker.domain.Combatant;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class StartEncounterUseCaseTest {

    @Test
    void happyPath() {
        EncounterRepositoryPort encounterRepositoryPort = mock(EncounterRepositoryPort.class);
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
        CombatEncounter encounter = CombatEncounter.createWithCombatants("encounter1", combatants);
        when(encounterRepositoryPort.getActive()).thenReturn(Optional.of(encounter));

        new StartEncounterUseCase(encounterRepositoryPort).execute(playerInitiatives);

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
