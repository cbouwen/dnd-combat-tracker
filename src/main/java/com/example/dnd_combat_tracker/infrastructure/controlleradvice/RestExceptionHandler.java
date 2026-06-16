package com.example.dnd_combat_tracker.infrastructure.controlleradvice;

import com.example.dnd_combat_tracker.application.exceptions.EncounterNotFoundException;
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
}
