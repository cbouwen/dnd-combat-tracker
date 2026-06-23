package com.example.dnd_combat_tracker.infrastructure.controlleradvice;

import com.example.dnd_combat_tracker.application.exceptions.TemplateIdRequiredException;
import com.example.dnd_combat_tracker.domain.exceptions.CombatantNotFoundException;
import com.example.dnd_combat_tracker.application.exceptions.EncounterNotFoundException;
import com.example.dnd_combat_tracker.domain.exceptions.EncounterNotActiveException;
import com.example.dnd_combat_tracker.domain.exceptions.NotAllInitiativesSetException;
import com.example.dnd_combat_tracker.domain.exceptions.DuplicatePlayerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(EncounterNotFoundException.class)
    protected ResponseEntity<String> handleEncounterNotFound(EncounterNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header("Content-Type", MediaType.TEXT_PLAIN_VALUE)
                .body(ex.getMessage());
    }

    @ExceptionHandler(CombatantNotFoundException.class)
    protected ResponseEntity<String> handleCombatantNotFound(CombatantNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header("Content-Type", MediaType.TEXT_PLAIN_VALUE)
                .body(ex.getMessage());
    }

    @ExceptionHandler(NotAllInitiativesSetException.class)
    protected ResponseEntity<String> handleNotAllInitiativesSet(NotAllInitiativesSetException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .header("Content-Type", MediaType.TEXT_PLAIN_VALUE)
                .body(ex.getMessage());
    }

    @ExceptionHandler(DuplicatePlayerException.class)
    protected ResponseEntity<String> handleDuplicatePlayerAdded(DuplicatePlayerException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .header("Content-Type", MediaType.TEXT_PLAIN_VALUE)
                .body(ex.getMessage());
    }

    @ExceptionHandler(TemplateIdRequiredException.class)
    protected ResponseEntity<String> handleNoTemplateId(TemplateIdRequiredException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .header("Content-Type", MediaType.TEXT_PLAIN_VALUE)
                .body(ex.getMessage());
    }

    @ExceptionHandler(EncounterNotActiveException.class)
    protected ResponseEntity<String> handleEncounterNotActive(EncounterNotActiveException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .header("Content-Type", MediaType.TEXT_PLAIN_VALUE)
                .body(ex.getMessage());
    }
}
