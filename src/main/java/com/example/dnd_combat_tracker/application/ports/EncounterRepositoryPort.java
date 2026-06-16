package com.example.dnd_combat_tracker.application.ports;

import com.example.dnd_combat_tracker.domain.CombatEncounter;

import java.util.Optional;

public interface EncounterRepositoryPort {
    void save(CombatEncounter encounter);
    Optional<CombatEncounter> findById(String id);
    //TODO: Remove all wrongful usages of getActive()
    Optional<CombatEncounter> getActive();
    void clear();
}
