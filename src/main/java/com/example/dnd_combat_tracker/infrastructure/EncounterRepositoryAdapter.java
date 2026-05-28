package com.example.dnd_combat_tracker.infrastructure;

import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EncounterRepositoryAdapter implements EncounterRepositoryPort {
    private CombatEncounter activeEncounter;

    @Override
    public void save(CombatEncounter encounter) {
            this.activeEncounter = encounter;
    }

    @Override
    public Optional<CombatEncounter> getActive() {
        return Optional.ofNullable(this.activeEncounter);
    }

    @Override
    public void clear() {
        this.activeEncounter = null;
    }
}
