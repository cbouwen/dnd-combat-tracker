package com.example.dnd_combat_tracker.domain;

import com.example.dnd_combat_tracker.domain.exceptions.CombatantNotFoundException;
import com.example.dnd_combat_tracker.domain.exceptions.DuplicatePlayerException;
import com.example.dnd_combat_tracker.domain.exceptions.NotAllInitiativesSetException;
import com.example.dnd_combat_tracker.domain.exceptions.EncounterNotActiveException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

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

    public void setInitiatives(Map<String, Integer> playerInitiatives) {
        combatants.stream()
                .filter(c -> c.getType() == CombatantType.PC)
                .forEach(pc -> {
                    if (!playerInitiatives.containsKey(pc.getId())) {
                        throw new NotAllInitiativesSetException("Missing initiative for PC: " + pc.getName());
                    }
                });

        playerInitiatives.forEach((combatantId, initiative) -> {
            Combatant combatant = findCombatantById(combatantId)
                    .orElseThrow(() -> new CombatantNotFoundException(combatantId));
            if (initiative == null) {
                throw new NotAllInitiativesSetException("Initiative cannot be null for combatant: " + combatantId);
            }
            combatant.setInitiative(initiative);
        });

        combatants.stream()
                .filter(c -> c.getType() != CombatantType.PC)
                .forEach(c -> c.setInitiative(rollD20()));
    }

    public void endEncounter() {
        this.state = EncounterState.ENDED;
    }

    public Optional<Combatant> findCombatantById(String combatantId) {
        return this.combatants.stream()
                .filter(combatant -> combatant.getId().equals(combatantId))
                .findFirst();
    }

    public void addCombatant(Combatant newCombatant) {
        if (state == EncounterState.ENDED) {
            throw new EncounterNotActiveException("Cannot add combatant to ended encounter");
        }
        if (newCombatant.getType() == CombatantType.PC) {
            boolean pcAlreadyExists = combatants.stream()
                    .anyMatch(combatant -> combatant.getType() == CombatantType.PC &&
                            newCombatant.getName().equals(combatant.getName()));
            if (pcAlreadyExists) {
                throw new DuplicatePlayerException(newCombatant.getName());
            }
        }
        this.combatants.add(newCombatant);
    }

    public void damageCombatant(String combatantId, int damage) {
        if (state != EncounterState.ACTIVE) {
            throw new EncounterNotActiveException("Encounter is not active");
        }
        Combatant combatant = findCombatantById(combatantId).orElseThrow(() -> new CombatantNotFoundException(combatantId));
        combatant.takeDamage(damage);
    }

    public void healCombatant(String combatantId, int healAmount) {
        if (state != EncounterState.ACTIVE) {
            throw new EncounterNotActiveException("Encounter is not active");
        }
        Combatant combatant = findCombatantById(combatantId).orElseThrow(() -> new CombatantNotFoundException(combatantId));
        combatant.heal(healAmount);
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

    private static int rollD20() {
        return ThreadLocalRandom.current().nextInt(1, 21);
    }

    public String getId() {
        return id;
    }

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
