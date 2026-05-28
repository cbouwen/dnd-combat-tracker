package com.example.dnd_combat_tracker.infrastructure.controllers;

import com.example.dnd_combat_tracker.application.CreateEncounterUseCase;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import com.example.dnd_combat_tracker.domain.Combatant;
import com.example.dnd_combat_tracker.infrastructure.dtos.CombatEncounterDTO;
import com.example.dnd_combat_tracker.infrastructure.dtos.CombatantDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/encounters")
public class CombatEncounterController {
    private final CreateEncounterUseCase createEncounterUseCase;

    public CombatEncounterController(CreateEncounterUseCase createEncounterUseCase) {
        this.createEncounterUseCase = createEncounterUseCase;
    }

    @PostMapping
    public ResponseEntity<CombatEncounterDTO> createEncounter() {
        CombatEncounter combatEncounter = createEncounterUseCase.execute();
        CombatEncounterDTO combatEncounterDTO = toCombatEncounterDTO(combatEncounter);
        return ResponseEntity.status(HttpStatus.CREATED).body(combatEncounterDTO);
    }

    private CombatEncounterDTO toCombatEncounterDTO(CombatEncounter encounter) {
        return new CombatEncounterDTO(
                encounter.getId(),
                encounter.getState().toString(),
                encounter.getCombatants().stream()
                        .map(this::toCombatantDTO)
                        .toList()
        );
    }

    private CombatantDTO toCombatantDTO(Combatant combatant) {
        return new CombatantDTO(
                combatant.getId(),
                combatant.getName(),
                combatant.getType().toString(),
                combatant.getCurrentHP(),
                combatant.getMaxHP(),
                combatant.getAc(),
                combatant.getInitiative(),
                combatant.getInitiativeModifier()
        );
    }

}
