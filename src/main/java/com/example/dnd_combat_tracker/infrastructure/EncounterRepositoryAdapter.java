package com.example.dnd_combat_tracker.infrastructure;

import com.example.dnd_combat_tracker.application.ports.EncounterRepositoryPort;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class EncounterRepositoryAdapter implements EncounterRepositoryPort {
    private final Map<String, CombatEncounter> encounters = new HashMap<>();

    @Override
    public void save(CombatEncounter encounter) {
            encounters.put(encounter.getId(), encounter);
    }

    @Override
    public Optional<CombatEncounter> findById(String id) {
        return Optional.ofNullable(encounters.get(id));
    }

    //TODO: This works for 1 DM. Once we implement multiple users, this breaks.
    @Override
    public Optional<CombatEncounter> getActive() {
        return encounters.values().stream().filter(e -> e.getState() == CombatEncounter.EncounterState.ACTIVE).findFirst();
    }

    @Override
    public void clear() {
        encounters.clear();
    }
}
