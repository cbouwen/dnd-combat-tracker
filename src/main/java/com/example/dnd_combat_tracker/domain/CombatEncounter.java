package com.example.dnd_combat_tracker.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CombatEncounter {
    private final String id;
    private final List<Combatant> combatants;
    private int currentTurn;
    private EncounterState state;

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
                List.of(),
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

    public void addCombatant(Combatant combatant) {
        if (state == EncounterState.ENDED) {
            throw new IllegalStateException("Cannot add combatant to ended encounter");
        }
        this.combatants.add(combatant);
    }

    public void removeCombatant(Combatant combatant) {
        if (!this.combatants.remove(combatant)) {
            throw new IllegalArgumentException("Combatant not found in encounter");
        }
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
            throw new IllegalStateException("Encounter is not active");
        this.currentTurn = (this.currentTurn + 1) % combatants.size();
    }

    public void previousTurn() {
        if (this.state != EncounterState.ACTIVE)
            throw new IllegalStateException("Encounter is not active");
        this.currentTurn = (this.currentTurn - 1) % combatants.size();
    }

}

enum EncounterState {
    PREPARING,
    ACTIVE,
    ENDED
}
