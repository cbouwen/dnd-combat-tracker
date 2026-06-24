package com.example.dnd_combat_tracker.infrastructure.controllers;

import com.example.dnd_combat_tracker.application.usecases.*;
import com.example.dnd_combat_tracker.domain.CombatEncounter;
import com.example.dnd_combat_tracker.domain.Combatant;
import com.example.dnd_combat_tracker.infrastructure.dtos.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encounters")
public class CombatEncounterController {
    private final CreateEncounterUseCase createEncounterUseCase;
    private final AddCombatantUseCase addCombatantUseCase;
    private final StartEncounterUseCase startEncounterUseCase;
    private final GetEncounterUseCase getEncounterUseCase;
    private final NextTurnUseCase nextTurnUseCase;
    private final RemoveCombatantUseCase removeCombatantUseCase;
    private final PreviousTurnUseCase previousTurnUseCase;
    private final DealDamageUseCase dealDamageUseCase;
    private final HealDamageUseCase healDamageUseCase;
    private final EndEncounterUseCase endEncounterUseCase;
    private final GetAllEncountersUseCase getAllEncountersUseCase;

    public CombatEncounterController(
            CreateEncounterUseCase createEncounterUseCase,
            AddCombatantUseCase addCombatantUseCase,
            StartEncounterUseCase startEncounterUseCase,
            GetEncounterUseCase getEncounterUseCase,
            NextTurnUseCase nextTurnUseCase,
            RemoveCombatantUseCase removeCombatantUseCase,
            PreviousTurnUseCase previousTurnUseCase,
            DealDamageUseCase dealDamageUseCase,
            HealDamageUseCase healDamageUseCase,
            EndEncounterUseCase endEncounterUseCase,
            GetAllEncountersUseCase getAllEncountersUseCase
    ) {
        this.createEncounterUseCase = createEncounterUseCase;
        this.addCombatantUseCase = addCombatantUseCase;
        this.startEncounterUseCase = startEncounterUseCase;
        this.getEncounterUseCase = getEncounterUseCase;
        this.nextTurnUseCase = nextTurnUseCase;
        this.removeCombatantUseCase = removeCombatantUseCase;
        this.previousTurnUseCase = previousTurnUseCase;
        this.dealDamageUseCase = dealDamageUseCase;
        this.healDamageUseCase = healDamageUseCase;
        this.endEncounterUseCase = endEncounterUseCase;
        this.getAllEncountersUseCase = getAllEncountersUseCase;
    }

    @PostMapping
    public ResponseEntity<CombatEncounterResponse> createEncounter() {
        CombatEncounter combatEncounter = createEncounterUseCase.execute();
        return ResponseEntity.status(HttpStatus.CREATED).body(CombatEncounterResponse.from(combatEncounter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CombatEncounterResponse> getEncounter(@PathVariable String id) {
        CombatEncounter combatEncounter = getEncounterUseCase.execute(id);
        return ResponseEntity.ok(CombatEncounterResponse.from(combatEncounter));
    }

    @GetMapping()
    public ResponseEntity<List<CombatEncounterResponse>> getEncounters() {
        List<CombatEncounterResponse> combatEncounterResponses = getAllEncountersUseCase.execute().stream().map(CombatEncounterResponse::from).toList();
        return ResponseEntity.ok(combatEncounterResponses);
    }

    @PostMapping("/{id}/combatants")
    public ResponseEntity<CombatantResponse> addCombatant(@RequestBody AddCombatantRequest addCombatantRequest, @PathVariable String id) {
        Combatant combatant = addCombatantUseCase.execute(addCombatantRequest.toCommand(id));
        return ResponseEntity.status(HttpStatus.CREATED).body(CombatantResponse.from(combatant));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<CombatEncounterResponse> startEncounter(@RequestBody StartEncounterRequest startEncounterRequest, @PathVariable String id) {
        CombatEncounter combatEncounter = startEncounterUseCase.execute(startEncounterRequest.toCommand(id));
        return ResponseEntity.ok(CombatEncounterResponse.from(combatEncounter));
    }

    @PostMapping("/{id}/next-turn")
    public ResponseEntity<CombatEncounterResponse> nextTurn(@PathVariable String id) {
        CombatEncounter combatEncounter = nextTurnUseCase.execute(id);
        return ResponseEntity.ok(CombatEncounterResponse.from(combatEncounter));
    }

    @PostMapping("/{id}/previous-turn")
    public ResponseEntity<CombatEncounterResponse> previousTurn(@PathVariable String id) {
        CombatEncounter combatEncounter = previousTurnUseCase.execute(id);
        return ResponseEntity.ok(CombatEncounterResponse.from(combatEncounter));
    }

    @PostMapping("/{encounterId}/combatants/{combatantId}/damage")
    public ResponseEntity<CombatEncounterResponse> dealDamage(
            @RequestBody DealDamageRequest dealDamageRequest,
            @PathVariable String encounterId,
            @PathVariable String combatantId
    ) {
        CombatEncounter combatEncounter = dealDamageUseCase.execute(encounterId, combatantId, dealDamageRequest.amount());
        return ResponseEntity.ok(CombatEncounterResponse.from(combatEncounter));
    }

    @PostMapping("/{encounterId}/combatants/{combatantId}/heal")
    public ResponseEntity<CombatEncounterResponse> healDamage(
            @RequestBody HealRequest healRequest,
            @PathVariable String encounterId,
            @PathVariable String combatantId
    ) {
        CombatEncounter combatEncounter = healDamageUseCase.execute(encounterId, combatantId, healRequest.amount());
        return ResponseEntity.ok(CombatEncounterResponse.from(combatEncounter));
    }

    @PostMapping("/{encounterId}/end")
    public ResponseEntity<CombatEncounterResponse> endEncounter(@PathVariable String encounterId) {
        CombatEncounter combatEncounter = endEncounterUseCase.execute(encounterId);
        return ResponseEntity.ok(CombatEncounterResponse.from(combatEncounter));
    }

    @DeleteMapping("/{encounterId}/combatants/{combatantId}")
    public ResponseEntity<Void> removeCombatant(
            @PathVariable String encounterId,
            @PathVariable String combatantId
    ) {
        removeCombatantUseCase.execute(encounterId, combatantId);
        return ResponseEntity.noContent().build();
    }
}
