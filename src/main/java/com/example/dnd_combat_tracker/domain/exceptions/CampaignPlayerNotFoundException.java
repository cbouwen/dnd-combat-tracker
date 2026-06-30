package com.example.dnd_combat_tracker.domain.exceptions;

public class CampaignPlayerNotFoundException extends RuntimeException {
    public CampaignPlayerNotFoundException(String playerId) {
        super("No player found for id: " + playerId);
    }
}
