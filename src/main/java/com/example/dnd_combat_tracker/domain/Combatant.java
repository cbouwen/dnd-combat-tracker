package com.example.dnd_combat_tracker.domain;

public class Combatant {
    private final String id;
    private final String name;
    private final String templateId;
    private final int maxHP;
    private int currentHP;
    private final int ac;
    private Integer initiative;
    private int initiativeModifier;
    private final CombatantType type;

    private Combatant(
            String id,
            String name,
            String templateId,
            int maxHP,
            int currentHP,
            int ac,
            Integer initiative,
            int initiativeModifier,
            CombatantType type
    ) {
        this.id = id;
        this.name = name;
        this.templateId = templateId;
        this.maxHP = maxHP;
        this.currentHP = currentHP;
        this.ac = ac;
        this.initiative = initiative;
        this.initiativeModifier = initiativeModifier;
        this.type = type;
    }


    public static Combatant createPlayer(
            String id,
            String name,
            int maxHP,
            int ac,
            int initiativeModifier
    ) {
        return new Combatant(
                id,
                name,
                null,
                maxHP,
                maxHP,
                ac,
                null,
                initiativeModifier,
                CombatantType.PC
        );
    }

    public static Combatant createEnemy(
            String id,
            String name,
            String templateId,
            int maxHP,
            int ac,
            int initiativeModifier
    ) {
        return new Combatant(
                id,
                name,
                templateId,
                maxHP,
                maxHP,
                ac,
                null,
                initiativeModifier,
                CombatantType.ENEMY
        );
    }

    public static Combatant createNPC(
            String id,
            String name,
            String templateId,
            int maxHP,
            int ac,
            int initiativeModifier
    ) {
        return new Combatant(
                id,
                name,
                templateId,
                maxHP,
                maxHP,
                ac,
                null,
                initiativeModifier,
                CombatantType.NPC
        );
    }

}

enum CombatantType {
    PC, NPC, ENEMY
}
