package com.rsh.p20260804001.game;

import com.rsh.p20260804001.server.GameServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Pruebas de la entidad enemiga de la feature 0013. */
class EnemyTest
{
    /** Ruta del código del enemigo. */
    private static final Path ENEMY_JS = Path.of("webcontent/js/entity/Enemy.js");

    /** Cliente HTTP de pruebas. */
    private HttpClient client;

    /** Puerto del servidor de pruebas. */
    private int port;

    /** Servidor bajo prueba. */
    private GameServer server;

    /** Arranca un servidor en un puerto libre. */
    @BeforeEach
    void setUp() throws IOException
    {
        server = new GameServer(0);
        server.init(0);
        port = server.getPort();
        client = HttpClient.newHttpClient();
    }

    /** Detiene el servidor. */
    @AfterEach
    void tearDown()
    {
        server.stop();
    }

    /** Comprueba la herencia y el estado principal. */
    @Test
    void enemyExtendsEntityAndDeclaresCombatState() throws IOException
    {
        String enemy = Files.readString(ENEMY_JS);
        assertTrue(enemy.contains("class Enemy extends Entity"));
        assertTrue(enemy.contains("this.enemyType"));
        assertTrue(enemy.contains("this.health"));
        assertTrue(enemy.contains("this.minDamage"));
        assertTrue(enemy.contains("this.maxDamage"));
        assertTrue(enemy.contains("this.minDefense"));
        assertTrue(enemy.contains("this.maxDefense"));
        assertTrue(enemy.contains("this.attackDelay"));
    }

    /** Comprueba las fórmulas dependientes del nivel y del tipo. */
    @Test
    void enemyCalculatesStatsFromLevelAndType() throws IOException
    {
        String enemy = Files.readString(ENEMY_JS);
        assertTrue(enemy.contains("currentLevel + ENEMY_LEVEL_OFFSET"));
        assertTrue(enemy.contains("ENEMY_HEALTH_LEVEL_FACTOR = 10"));
        assertTrue(enemy.contains("ENEMY_MIN_DAMAGE_LEVEL_FACTOR = 2"));
        assertTrue(enemy.contains("ENEMY_MAX_DAMAGE_LEVEL_FACTOR = 5"));
        assertTrue(enemy.contains("ENEMY_MAX_DEFENSE_LEVEL_FACTOR = 2"));
        assertTrue(enemy.contains("this.enemyType * ENEMY_ATTACK_DELAY_FACTOR"));
    }

    /** Comprueba el combate y la muerte del enemigo. */
    @Test
    void enemyImplementsCombatAndDeath() throws IOException
    {
        String enemy = Files.readString(ENEMY_JS);
        assertTrue(enemy.contains("attack(player)"));
        assertTrue(enemy.contains("player.defend(damage)"));
        assertTrue(enemy.contains("defend(damage)"));
        assertTrue(enemy.contains("this.health = Math.max"));
        assertTrue(enemy.contains("isDead()"));
    }

    /** Comprueba los ocho movimientos solicitados. */
    @Test
    void enemyDeclaresEightDirections() throws IOException
    {
        String enemy = Files.readString(ENEMY_JS);
        String[] movements = {"moveUp()", "moveDown()", "moveLeft()", "moveRight()",
            "moveUpLeft()", "moveUpRight()", "moveDownLeft()", "moveDownRight()"};
        for (String movement : movements)
        {
            assertTrue(enemy.contains(movement), "Falta " + movement);
        }
        assertTrue(enemy.contains("this.isVisitable(targetX, targetY)"));
    }

    /** Comprueba que el servidor publica Enemy.js. */
    @Test
    void enemyResourceIsServed() throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + port + "/js/entity/Enemy.js")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("Content-Type").orElse("")
                .startsWith("application/javascript"));
        assertTrue(response.body().contains("class Enemy extends Entity"));
    }
}
