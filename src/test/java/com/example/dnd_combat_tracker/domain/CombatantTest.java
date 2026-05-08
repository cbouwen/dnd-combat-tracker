package com.example.dnd_combat_tracker.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CombatantTest {

    @Test
    void createPlayerShouldCreatePlayerWithCorrectType() {
        // When
        Combatant player = Combatant.createPlayer(
                "1",
                "tester",
                20,
                15,
                3
        );

        // Then
        assertThat(player.getType()).isEqualTo(CombatantType.PC);
    }

    @Test
    void createEnemyShouldCreatePlayerWithCorrectType() {
        // When
        Combatant enemy = Combatant.createEnemy(
                "1",
                "tester",
                null,
                20,
                15,
                3
        );

        // Then
        assertThat(enemy.getType()).isEqualTo(CombatantType.ENEMY);
    }

    @Test
    void createNPCShouldCreatePlayerWithCorrectType() {
        // When
        Combatant npc = Combatant.createNPC(
                "1",
                "tester",
                null,
                20,
                15,
                3
        );

        // Then
        assertThat(npc.getType()).isEqualTo(CombatantType.NPC);
    }

    @Test
    void takeDamageShouldLowerHP() {
        Combatant player = Combatant.createPlayer(
                "1",
                "tester",
                20,
                15,
                3
        );

        player.takeDamage(6);

        assertThat(player.getCurrentHP()).isEqualTo(14);
    }

    @Test
    void damageShouldNotGoUnderZero() {
        Combatant player = Combatant.createPlayer(
                "1",
                "tester",
                20,
                15,
                3
        );

        player.takeDamage(100);

        assertThat(player.getCurrentHP()).isEqualTo(0);
    }

    @Test
    void negativeDamageShouldThrowIllegalArgumentException() {
        Combatant player = Combatant.createPlayer(
                "1",
                "tester",
                20,
                15,
                3
        );

        assertThrows(IllegalArgumentException.class, () -> player.takeDamage(-5));
    }

    @Test
    void healShouldAddHP() {
        Combatant player = Combatant.createPlayer(
                "1",
                "tester",
                20,
                15,
                3
        );

        player.takeDamage(14);
        player.heal(6);

        assertThat(player.getCurrentHP()).isEqualTo(12);
    }


    @Test
    void healShouldNotGoOverMaxHealth() {
        Combatant player = Combatant.createPlayer(
                "1",
                "tester",
                20,
                15,
                3
        );

        player.takeDamage(14);
        player.heal(100);

        assertThat(player.getCurrentHP()).isEqualTo(20);
    }

    @Test
    void negativeHealingShouldThrowIllegalArgumentException() {
        Combatant player = Combatant.createPlayer(
                "1",
                "tester",
                20,
                15,
                3
        );

        assertThrows(IllegalArgumentException.class, () -> player.heal(-5));
    }

    @Test
    void setInitiativeShouldDifferForPlayersAndEnemies() {
        Combatant player = Combatant.createPlayer(
                "1",
                "Zeraack",
                20,
                15,
                3
        );
        Combatant enemy = Combatant.createEnemy(
                "2",
                "Bandit",
                null,
                14,
                14,
                1
        );

        player.setInitiative(15);
        enemy.setInitiative(15);

        assertThat(player.getInitiative()).isEqualTo(15);
        assertThat(enemy.getInitiative()).isEqualTo(16);
    }
}
