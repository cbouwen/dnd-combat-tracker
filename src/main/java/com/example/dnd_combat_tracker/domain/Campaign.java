package com.example.dnd_combat_tracker.domain;

import com.example.dnd_combat_tracker.domain.exceptions.CampaignPlayerNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Campaign {
    private final String id;
    private String name;
    private final List<CampaignPlayer> playerCharacters;
    private final List<String> encounters;

    private Campaign(
            String id,
            String name,
            List<CampaignPlayer> playerCharacters,
            List<String> encounters
    ) {
        this.id = id;
        this.name = name;
        this.playerCharacters = playerCharacters;
        this.encounters = encounters;
    }

    public static Campaign create(String name) {
        return new Campaign(
                UUID.randomUUID().toString(),
                name,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public void addEncounterId(String combatEncounterId) {
        encounters.add(combatEncounterId);
    }

    public void addPlayer(CampaignPlayer campaignPlayer) {
        playerCharacters.add(campaignPlayer);
    }

    public void removePlayer(String playerId) {
        CampaignPlayer campaignPlayer = playerCharacters.stream()
                .filter(pc -> pc.getId().equals(playerId))
                .findFirst().orElseThrow(() -> new CampaignPlayerNotFoundException(playerId));
        playerCharacters.remove(campaignPlayer);
    }

    public void updatePlayer(CampaignPlayer updatedPlayer) {
        CampaignPlayer campaignPlayer = playerCharacters.stream()
                .filter(pc -> pc.getId().equals(updatedPlayer.getId()))
                .findFirst().orElseThrow(() -> new CampaignPlayerNotFoundException(updatedPlayer.getId()));
        int index = playerCharacters.indexOf(campaignPlayer);
        playerCharacters.set(index, updatedPlayer);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CampaignPlayer> getPlayerCharacters() {
        return Collections.unmodifiableList(playerCharacters);
    }

    public List<String> getEncounters() {
        return Collections.unmodifiableList(encounters);
    }
}
