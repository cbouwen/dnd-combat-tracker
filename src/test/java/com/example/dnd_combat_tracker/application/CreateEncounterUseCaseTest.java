package com.example.dnd_combat_tracker.application;

import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.application.usecases.CreateEncounterUseCase;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

class CreateEncounterUseCaseTest {

    @Test
    void happyPath() {
        EncounterRepositoryPort encounterRepositoryPort = mock(EncounterRepositoryPort.class);

        CombatEncounter activeEncounter = new CreateEncounterUseCase(encounterRepositoryPort).execute();

        assertThat(activeEncounter.getState()).isEqualTo(CombatEncounter.EncounterState.PREPARING);
        assertThat(activeEncounter.getCombatants().size()).isEqualTo(0);
        verify(encounterRepositoryPort).save(activeEncounter);
    }
}