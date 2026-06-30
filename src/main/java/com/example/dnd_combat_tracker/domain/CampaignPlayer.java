package com.example.dnd_combat_tracker.domain;

import java.util.UUID;

public class CampaignPlayer {
    private final String id;
    private final String name;
    private final int maxHp;
    private final int ac;
    private final int initiativeModifier;

    public CampaignPlayer(
            String id,
            String name,
            int maxHp,
            int ac,
            int initiativeModifier
    ) {
        this.id = id;
        this.name = name;
        this.maxHp = maxHp;
        this.ac = ac;
        this.initiativeModifier = initiativeModifier;
    }

    public static CampaignPlayer create(
            String name,
            int maxHp,
            int ac,
            int initiativeModifier
    ) {
        return new CampaignPlayer(
                UUID.randomUUID().toString(),
                name,
                maxHp,
                ac,
                initiativeModifier
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAc() {
        return ac;
    }

    public int getInitiativeModifier() {
        return initiativeModifier;
    }

}
