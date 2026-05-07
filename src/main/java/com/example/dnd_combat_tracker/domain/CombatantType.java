package com.example.dnd_combat_tracker.domain;

public enum CombatantType {
    PC(1),
    NPC(2),
    ENEMY(3);

    private final int priority;

    CombatantType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }
}
