package com.example.dnd_combat_tracker.domain;

import java.util.ArrayList;
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

    public void addCombatant(Combatant combatant) {
        if (state == EncounterState.ENDED) {
            throw new IllegalStateException("Cannot add combatant to ended encounter");
        }
        this.combatants.add(combatant);
    }

    public void removeCombatant(Combatant combatant) {
        if (!this.combatants.remove(combatant))
            System.out.println("Cannot find combatant");
    }
}

enum EncounterState {
    PREPARING,
    ACTIVE,
    ENDED
}
