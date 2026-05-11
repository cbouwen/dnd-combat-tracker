package com.example.dnd_combat_tracker.application;

import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateEncounterUseCaseTest {

    @Test
    void happyPath() {
        EncounterRepositoryPort encounterRepositoryPort = mock(EncounterRepositoryPort.class);

        CombatEncounter activeEncounter = new CreateEncounterUseCase(encounterRepositoryPort).execute();
        when(encounterRepositoryPort.getActive()).thenReturn(Optional.of(activeEncounter));

        assertThat(activeEncounter.getState()).isEqualTo(CombatEncounter.EncounterState.PREPARING);
        verify(encounterRepositoryPort).save(activeEncounter);
    }
}