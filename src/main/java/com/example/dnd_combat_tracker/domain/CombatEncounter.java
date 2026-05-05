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
}

enum EncounterState {
    PREPARING,
    ACTIVE,
    ENDED
}
