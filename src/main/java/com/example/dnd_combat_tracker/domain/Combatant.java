package com.example.dnd_combat_tracker.domain;

import java.util.UUID;

public class Combatant {
    private final String id;
    private final String name;
    private final String templateId;
    private final int maxHP;
    private int currentHP;
    private final int ac;
    private Integer initiative;
    private final int initiativeModifier;
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

    public static Combatant create(
            CombatantType type,
            String name,
            String templateId,
            int maxHP,
            int ac,
            int initiativeModifier
    ) {
        return switch (type) {
            case PC -> createPlayer(UUID.randomUUID().toString(), name, maxHP, ac, initiativeModifier);
            case NPC -> createNPC(UUID.randomUUID().toString(), name, templateId, maxHP, ac, initiativeModifier);
            case ENEMY -> createEnemy(UUID.randomUUID().toString(), name, templateId, maxHP, ac, initiativeModifier);
        };
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
                "",
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

    public void takeDamage(int damageAmount) {
        if (damageAmount < 0) {
            throw new IllegalArgumentException("Damage cannot be negative");
        }
        this.currentHP = Math.max(0, this.currentHP - damageAmount);
    }

    public void heal(int healAmount) {
        if (healAmount < 0) {
            throw new IllegalArgumentException("Healing cannot be negative");
        }
        this.currentHP = Math.min(this.maxHP, this.currentHP + healAmount);
    }

    public void setInitiative(int value) {
        if (this.type == CombatantType.PC) {
            this.initiative = value;
        } else {
            this.initiative = value + this.initiativeModifier;
        }
    }

    public Integer getInitiative() {
        return initiative;
    }

    public int getInitiativeModifier() {
        return initiativeModifier;
    }

    public CombatantType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public String getName() {
        return name;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getAc() {
        return ac;
    }
}
