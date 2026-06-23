package com.example.dnd_combat_tracker.domain;

import com.example.dnd_combat_tracker.domain.exceptions.CombatantNotFoundException;
import com.example.dnd_combat_tracker.domain.exceptions.NotAllInitiativesSetException;
import com.example.dnd_combat_tracker.domain.exceptions.EncounterNotActiveException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CombatEncounterTest {

    @Test
    void happyPath() {
        CombatEncounter combatEncounter = CombatEncounter.create("10");

        assertThat(combatEncounter.getState()).isEqualTo(CombatEncounter.EncounterState.PREPARING);
    }

    @Test
    void happyPathWithCombatants() {
        List<Combatant> combatants = List.of(
                Combatant.createPlayer("1", "Zeraack", 20, 15, 3),
                Combatant.createPlayer("2", "Solid", 30, 18, 1)
        );

        CombatEncounter combatEncounter = CombatEncounter.createWithCombatants("1800", combatants);

        assertThat(combatEncounter.getState()).isEqualTo(CombatEncounter.EncounterState.PREPARING);
        assertThat(combatEncounter.findCombatantById("1")).isPresent();
        assertThat(combatEncounter.findCombatantById("2")).isPresent();
    }

    @Test
    void addCombatantShouldAddToEncounter() {
        CombatEncounter combatEncounter = CombatEncounter.create("10");

        combatEncounter.addCombatant(Combatant.createPlayer("1", "Zeraack", 20, 15, 3));

        assertThat(combatEncounter.findCombatantById("1")).isPresent();
    }

    @Test
    void removeCombatantShouldRemoveFromEncounter() {
        CombatEncounter combatEncounter = createBasicEncounter();

        combatEncounter.removeCombatantById("1");

        assertThat(combatEncounter.findCombatantById("1")).isEmpty();
    }

    @Test
    void removeCombatantWithInvalidIdShouldThrowException() {
        CombatEncounter combatEncounter = createBasicEncounter();

        assertThrows(CombatantNotFoundException.class, () -> combatEncounter.removeCombatantById("5"));
    }

    @Test
    void nextTurnShouldGoToNextTurn() {
        CombatEncounter combatEncounter = createBasicEncounter();
        combatEncounter.startEncounter();

        combatEncounter.nextTurn();

        assertThat(combatEncounter.getCurrentTurn()).isEqualTo(1);
    }

    @Test
    void nextTurnOnFinalCombatantShouldRestartTurnIndex() {
        CombatEncounter combatEncounter = createBasicEncounter();
        combatEncounter.startEncounter();
        combatEncounter.nextTurn();
        combatEncounter.nextTurn();

        combatEncounter.nextTurn();

        assertThat(combatEncounter.getCurrentTurn()).isEqualTo(0);
    }

    @Test
    void nextTurnShouldThrowExceptionIfEncounterHasNotStartedYet() {
        CombatEncounter combatEncounter = createBasicEncounter();

        assertThrows(EncounterNotActiveException.class, combatEncounter::nextTurn);
    }

    @Test
    void previousTurnShouldDialBackTurn() {
        CombatEncounter combatEncounter = createBasicEncounter();
        combatEncounter.startEncounter();

        combatEncounter.previousTurn();

        assertThat(combatEncounter.getCurrentTurn()).isEqualTo(2);
    }

    @Test
    void startEncounterWithoutInitiativesShouldThrowException () {
        List<Combatant> combatants = List.of(
                Combatant.createPlayer("1", "Zeraack", 20, 15, 3),
                Combatant.createPlayer("2", "Solid", 30, 18, 1),
                Combatant.createPlayer("3", "Inhil", 30, 11, 2)
        );
        CombatEncounter combatEncounter = CombatEncounter.createWithCombatants("1800", combatants);

        assertThrows(NotAllInitiativesSetException.class, combatEncounter::startEncounter);
    }

    @Test
    void startEncounterShouldSortByInitiativeDescending() {
        List<Combatant> combatants = List.of(
                Combatant.createPlayer("1", "Zeraack", 20, 15, 3),
                Combatant.createPlayer("2", "Solid", 30, 18, 1),
                Combatant.createPlayer("3", "Inhil", 15, 11, 2)
        );
        CombatEncounter combatEncounter = CombatEncounter.createWithCombatants("1800", combatants);
        combatEncounter.findCombatantById("1").orElseThrow(() -> new CombatantNotFoundException("1")).setInitiative(15);
        combatEncounter.findCombatantById("2").orElseThrow(() -> new CombatantNotFoundException("2")).setInitiative(10);
        combatEncounter.findCombatantById("3").orElseThrow(() -> new CombatantNotFoundException("3")).setInitiative(20);

        combatEncounter.startEncounter();

        List<Combatant> sorted = combatEncounter.getCombatants();
        assertThat(sorted.get(0).getId()).isEqualTo("3");
        assertThat(sorted.get(1).getId()).isEqualTo("1");
        assertThat(sorted.get(2).getId()).isEqualTo("2");
    }

    @Test
    void addCombatantToEndedEncounterShouldThrowException() {
        CombatEncounter combatEncounter = createBasicEncounter();
        combatEncounter.endEncounter();

        assertThrows(EncounterNotActiveException.class, () -> combatEncounter.addCombatant(Combatant.createPlayer("3", "Didix", 20, 14, 2)));
    }

    @Test
    void endEncounterShouldChangeStateToEnded() {
        CombatEncounter combatEncounter = createBasicEncounter();
        combatEncounter.startEncounter();

        combatEncounter.endEncounter();

        assertThat(combatEncounter.getState()).isEqualTo(CombatEncounter.EncounterState.ENDED);
    }

    private CombatEncounter createBasicEncounter() {
        List<Combatant> combatants = List.of(
                Combatant.createPlayer("1", "Zeraack", 20, 15, 3),
                Combatant.createPlayer("2", "Solid", 30, 18, 1),
                Combatant.createPlayer("3", "Inhil", 30, 11, 2)
        );
        CombatEncounter combatEncounter = CombatEncounter.createWithCombatants("1800", combatants);
        combatEncounter.getCombatants().forEach(combatant -> combatant.setInitiative(15));
        return combatEncounter;
    }
}