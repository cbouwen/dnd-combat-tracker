package com.example.dnd_combat_tracker.domain;

import com.example.dnd_combat_tracker.application.exceptions.CombatantNotFoundException;
import com.example.dnd_combat_tracker.application.exceptions.NotAllInitiativesSetException;
import com.example.dnd_combat_tracker.domain.exceptions.EncounterNotActiveException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CombatEncounter {
    private final String id;
    private final List<Combatant> combatants;
    private int currentTurn;
    private EncounterState state;
    public enum EncounterState {
        PREPARING,
        ACTIVE,
        ENDED
    }

    private CombatEncounter(
            String id,
            List<Combatant> combatants,
            int currentTurn,
            EncounterState state
    ) {
        this.id = id;
        this.combatants = combatants;
        this.currentTurn = currentTurn;
        this.state = state;
    }

    public static CombatEncounter create(String id) {
        return new CombatEncounter(
                id,
                new ArrayList<>(),
                0,
                EncounterState.PREPARING
        );
    }

    public static CombatEncounter createWithCombatants(String id, List<Combatant> combatants) {
        return new CombatEncounter(
                id,
                new ArrayList<>(combatants),
                0,
                EncounterState.PREPARING
        );
    }

    public void startEncounter() {
        if (combatants.stream().anyMatch(c -> c.getInitiative() == null)) {
            throw new NotAllInitiativesSetException("Not all initiatives have been set");
        }
        sortByInitiative();
        this.state = EncounterState.ACTIVE;
    }

    public void endEncounter() {
        this.state = EncounterState.ENDED;
    }

    public Optional<Combatant> findCombatantById(String combatantId) {
        return this.combatants.stream()
                .filter(combatant -> combatant.getId().equals(combatantId))
                .findFirst();
    }

    public void addCombatant(Combatant combatant) {
        if (state == EncounterState.ENDED) {
            throw new EncounterNotActiveException("Cannot add combatant to ended encounter");
        }
        this.combatants.add(combatant);
    }

    public void removeCombatantById(String combatantId) {
        Combatant combatant = findCombatantById(combatantId)
                .orElseThrow(() -> new CombatantNotFoundException(combatantId));
        this.combatants.remove(combatant);
    }

    private void sortByInitiative() {
        combatants.sort(
                Comparator.comparing(Combatant::getInitiative)
                        .thenComparing(c -> c.getType().getPriority())
                        .thenComparing(Combatant::getInitiativeModifier)
                        .reversed()
        );
    }

    public void nextTurn() {
        if (this.state != EncounterState.ACTIVE)
            throw new EncounterNotActiveException("Encounter is not active");
        this.currentTurn = (this.currentTurn + 1) % combatants.size();
    }

    public void previousTurn() {
        if (this.state != EncounterState.ACTIVE)
            throw new EncounterNotActiveException("Encounter is not active");
        this.currentTurn = (this.currentTurn - 1 + combatants.size()) % combatants.size();
    }

    public String getId() { return id;}

    public EncounterState getState() {
        return state;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public List<Combatant> getCombatants() {
        return combatants;
    }
}
