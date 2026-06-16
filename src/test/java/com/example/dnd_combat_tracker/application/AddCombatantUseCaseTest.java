package com.example.dnd_combat_tracker.application;

import com.example.dnd_combat_tracker.application.commands.AddCombatantCommand;
import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.application.usecases.AddCombatantUseCase;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import com.example.dnd_combat_tracker.domain.Combatant;
import com.example.dnd_combat_tracker.domain.CombatantType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class AddCombatantUseCaseTest {

    @Test
    void happyPath() {
        List<Combatant> combatants = List.of(
                Combatant.createPlayer("1", "Zeraack", 20, 15, 3),
                Combatant.createPlayer("2", "Solid", 30, 18, 1)
        );
        String combatEncounterId = UUID.randomUUID().toString();
        CombatEncounter activeEncounter = CombatEncounter.createWithCombatants(combatEncounterId, combatants);
        EncounterRepositoryPort encounterRepositoryPort = mock(EncounterRepositoryPort.class);
        when(encounterRepositoryPort.findById(combatEncounterId)).thenReturn(Optional.of(activeEncounter));
        AddCombatantCommand addCombatantCommand = new AddCombatantCommand(
                combatEncounterId,
                CombatantType.ENEMY,
                "bandit",
                "template-id",
                14,
                13,
                1
        );

        new AddCombatantUseCase(encounterRepositoryPort).execute(addCombatantCommand);

        assertThat(activeEncounter.getState()).isEqualTo(CombatEncounter.EncounterState.PREPARING);
        assertThat(activeEncounter.getCombatants().size()).isEqualTo(3);
        verify(encounterRepositoryPort).save(activeEncounter);
    }
}
