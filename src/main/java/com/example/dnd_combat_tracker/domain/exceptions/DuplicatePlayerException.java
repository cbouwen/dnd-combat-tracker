package com.example.dnd_combat_tracker.domain.exceptions;

public class DuplicatePlayerException extends RuntimeException {
    public DuplicatePlayerException(String playerName) {

        super("Cannot add " + playerName + ". Player is already added.");
    }
}
