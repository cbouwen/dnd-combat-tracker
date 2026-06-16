package com.example.dnd_combat_tracker.infrastructure.controllers;

import com.example.dnd_combat_tracker.application.usecases.AddCombatantUseCase;
import com.example.dnd_combat_tracker.application.usecases.CreateEncounterUseCase;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import com.example.dnd_combat_tracker.domain.Combatant;
import com.example.dnd_combat_tracker.infrastructure.dtos.AddCombatantRequest;
import com.example.dnd_combat_tracker.infrastructure.dtos.CombatEncounterResponse;
import com.example.dnd_combat_tracker.infrastructure.dtos.CombatantResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/encounters")
public class CombatEncounterController {
    private final CreateEncounterUseCase createEncounterUseCase;
    private final AddCombatantUseCase addCombatantUseCase;

    public CombatEncounterController(CreateEncounterUseCase createEncounterUseCase, AddCombatantUseCase addCombatantUseCase) {
        this.createEncounterUseCase = createEncounterUseCase;
        this.addCombatantUseCase = addCombatantUseCase;
    }

    @PostMapping
    public ResponseEntity<CombatEncounterResponse> createEncounter() {
        CombatEncounter combatEncounter = createEncounterUseCase.execute();
        return ResponseEntity.status(HttpStatus.CREATED).body(CombatEncounterResponse.from(combatEncounter));
    }

    @PostMapping("/{id}/combatants")
    public ResponseEntity<CombatantResponse> addCombatant(@RequestBody AddCombatantRequest addCombatantRequest, @PathVariable String id) {
        Combatant combatant = addCombatantUseCase.execute(addCombatantRequest.toCommand(id));
        return ResponseEntity.status(HttpStatus.CREATED).body(CombatantResponse.from(combatant));
    }
}
