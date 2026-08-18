package com.rsh.p20260804001.game;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Pruebas de movimiento visual y combate automático de la feature 0014. */
class CombatDynamicsTest
{
    /** Ruta de la entidad jugador. */
    private static final Path PLAYER_JS = Path.of("webcontent/js/entity/Player.js");

    /** Ruta de la entidad enemiga. */
    private static final Path ENEMY_JS = Path.of("webcontent/js/entity/Enemy.js");

    /** Ruta de la escena de juego. */
    private static final Path GAME_SCENE_JS = Path.of("webcontent/js/scene/GameScene.js");

    /** Comprueba que cada combatiente conserva su temporizador de ataque. */
    @Test
    void combatantsDeclareIndependentAttackIntervals() throws IOException
    {
        String player = Files.readString(PLAYER_JS);
        String enemy = Files.readString(ENEMY_JS);

        assertTrue(player.contains("this.attackDelay"));
        assertTrue(player.contains("this.attackInterval = null"));
        assertTrue(enemy.contains("this.attackDelay"));
        assertTrue(enemy.contains("this.attackInterval = null"));
    }

    /** Comprueba el inicio y la cancelación de los ataques automáticos. */
    @Test
    void gameSceneManagesCombatIntervals() throws IOException
    {
        String scene = Files.readString(GAME_SCENE_JS);

        assertTrue(scene.contains("setInterval("));
        assertTrue(scene.contains("clearInterval("));
        assertTrue(scene.contains("this.player.attackDelay * GAME_ATTACK_DELAY_MILLISECONDS"));
        assertTrue(scene.contains("enemy.attackDelay * GAME_ATTACK_DELAY_MILLISECONDS"));
        assertTrue(scene.contains("isEnemyAdjacent(enemy)"));
        assertTrue(scene.contains("stopCombatIntervals()"));
    }

    /** Comprueba que los ataques generan las dos entradas de log requeridas. */
    @Test
    void gameSceneLogsLastFiveCombatMessages() throws IOException
    {
        String scene = Files.readString(GAME_SCENE_JS);

        assertTrue(scene.contains("GAME_LOG_MAX_MESSAGES = 5"));
        assertTrue(scene.contains("El goblin realiza un ataque con "));
        assertTrue(scene.contains("Golpeas al goblin con un ataque de "));
        assertTrue(scene.contains("slice(-GAME_LOG_MAX_MESSAGES)"));
        assertTrue(scene.contains("this.combatMessages.join('\\n')"));
    }

    /** Comprueba la vibración visual limitada a cinco píxeles durante el combate. */
    @Test
    void fightingEnemiesReceiveRandomVisualJitter() throws IOException
    {
        String scene = Files.readString(GAME_SCENE_JS);

        assertTrue(scene.contains("GAME_COMBAT_JITTER_PIXELS = 5"));
        assertTrue(scene.contains("getEnemyCombatJitter(enemy)"));
        assertTrue(scene.contains("enemy.attackInterval === null"));
        assertTrue(scene.contains("+ jitterX"));
        assertTrue(scene.contains("+ jitterY"));
    }
}
