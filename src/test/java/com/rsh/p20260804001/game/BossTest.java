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

/** Pruebas del jefe final de la feature 0016. */
class BossTest
{
    /** Ruta de la entidad jefe. */
    private static final Path BOSS_JS = Path.of("webcontent/js/entity/Boss.js");

    /** Ruta del laberinto. */
    private static final Path MAZE_JS = Path.of("webcontent/js/Maze.js");

    /** Ruta de la escena de juego. */
    private static final Path GAME_SCENE_JS = Path.of("webcontent/js/scene/GameScene.js");

    /** Ruta de la página principal. */
    private static final Path INDEX_HTML = Path.of("webcontent/pages/index.html");

    /** Cliente HTTP de pruebas. */
    private HttpClient client;

    /** Puerto asignado al servidor. */
    private int port;

    /** Servidor bajo prueba. */
    private GameServer server;

    /** Arranca el servidor sobre un puerto libre. */
    @BeforeEach
    void setUp() throws IOException
    {
        server = new GameServer(0);
        server.init(0);
        port = server.getPort();
        client = HttpClient.newHttpClient();
    }

    /** Detiene el servidor tras cada prueba. */
    @AfterEach
    void tearDown()
    {
        server.stop();
    }

    /** Comprueba la herencia, el estado y las fórmulas del jefe. */
    @Test
    void bossExtendsEntityAndDeclaresCombatState() throws IOException
    {
        String boss = Files.readString(BOSS_JS);

        assertTrue(boss.contains("class Boss extends Entity"));
        assertTrue(boss.contains("this.health"));
        assertTrue(boss.contains("this.minDamage"));
        assertTrue(boss.contains("this.maxDamage"));
        assertTrue(boss.contains("this.minDefense"));
        assertTrue(boss.contains("this.maxDefense"));
        assertTrue(boss.contains("this.attackDelay"));
        assertTrue(boss.contains("this.attackInterval = null"));
    }

    /** Comprueba el combate, la muerte y los ocho movimientos. */
    @Test
    void bossImplementsCombatAndMovement() throws IOException
    {
        String boss = Files.readString(BOSS_JS);
        String[] movements = {"moveUp()", "moveDown()", "moveLeft()", "moveRight()",
            "moveUpLeft()", "moveUpRight()", "moveDownLeft()", "moveDownRight()"};

        assertTrue(boss.contains("attack(player)"));
        assertTrue(boss.contains("player.defend(damage)"));
        assertTrue(boss.contains("defend(damage)"));
        assertTrue(boss.contains("isDead()"));
        for (String movement : movements)
        {
            assertTrue(boss.contains(movement), "Falta " + movement);
        }
    }

    /** Comprueba la generación única y segura en el último nivel. */
    @Test
    void mazeGeneratesOneBossOnLastLevel() throws IOException
    {
        String maze = Files.readString(MAZE_JS);

        assertTrue(maze.contains("this.boss = null"));
        assertTrue(maze.contains("generateBossForLevel(matrix, currentLevel)"));
        assertTrue(maze.contains("this.depth - MAZE_LEVEL_NUMBER_OFFSET"));
        assertTrue(maze.contains("matrix[y][x] === MAZE_ROOM_EMPTY"));
        assertTrue(maze.contains("this.boss = new Boss"));
    }

    /** Comprueba la integración del jefe en movimiento y combate. */
    @Test
    void gameSceneIntegratesBossCombat() throws IOException
    {
        String scene = Files.readString(GAME_SCENE_JS);
        String player = Files.readString(Path.of("webcontent/js/entity/Player.js"));

        assertTrue(scene.contains("this.boss = this.maze.boss"));
        assertTrue(scene.contains("this.findOpponentAt(target.x, target.y)"));
        assertTrue(scene.contains("this.isEnemyAdjacent(this.boss)"));
        assertTrue(scene.contains("this.executeEnemyAttack(this.boss)"));
        assertTrue(scene.contains("this.boss.attackInterval = setInterval"));
        assertTrue(scene.contains("this.boss.attackDelay * GAME_ATTACK_DELAY_MILLISECONDS"));
        assertTrue(scene.contains("this.stopEnemyCombat(this.boss)"));
        assertTrue(player.contains("occupiedByBoss"));
    }

    /** Comprueba las reglas de representación del orco. */
    @Test
    void gameSceneRendersBossAtThreeDepths() throws IOException
    {
        String scene = Files.readString(GAME_SCENE_JS);

        assertTrue(scene.contains("Orc-001.png"));
        assertTrue(scene.contains("GAME_BOSS_FAR_SCALE = 0.25"));
        assertTrue(scene.contains("GAME_BOSS_MIDDLE_SCALE = 0.5"));
        assertTrue(scene.contains("GAME_BOSS_NEAR_SCALE = 1.0"));
        assertTrue(scene.contains("GAME_BOSS_FAR_VERTICAL_OFFSET = -160"));
        assertTrue(scene.contains("GAME_BOSS_MIDDLE_VERTICAL_OFFSET = -128"));
        assertTrue(scene.contains("GAME_BOSS_NEAR_VERTICAL_OFFSET = -64"));
        assertTrue(scene.contains("GAME_BOSS_FAR_ILLUMINATION = 0.25"));
        assertTrue(scene.contains("GAME_BOSS_MIDDLE_ILLUMINATION = 0.5"));
        assertTrue(scene.contains("GAME_BOSS_NEAR_ILLUMINATION = 1.0"));
        assertTrue(scene.contains("drawBoss(cell"));
        assertTrue(scene.contains("this.getEnemyCombatJitter(this.boss)"));
        assertTrue(scene.contains("+ jitterX"));
        assertTrue(scene.contains("+ jitterY"));
    }

    /** Comprueba los mensajes específicos del combate contra el orco. */
    @Test
    void gameSceneLogsBossCombatMessages() throws IOException
    {
        String scene = Files.readString(GAME_SCENE_JS);

        assertTrue(scene.contains("El orco realiza un ataque con "));
        assertTrue(scene.contains("Golpeas al orco con un ataque de "));
        assertTrue(scene.contains("this.isBoss(enemyAhead)"));
        assertTrue(scene.contains("this.isBoss(enemy)"));
    }

    /** Comprueba la retirada del jefe y la transición de victoria. */
    @Test
    void gameSceneHandlesBossDefeatAndVictory() throws IOException
    {
        String scene = Files.readString(GAME_SCENE_JS);

        assertTrue(scene.contains("this.maze.boss = null"));
        assertTrue(scene.contains("GAME_VICTORY_TEXT = 'You win!!!'"));
        assertTrue(scene.contains("GAME_VICTORY_DELAY_MILLISECONDS = 5000"));
        assertTrue(scene.contains("showVictory()"));
        assertTrue(scene.contains("this.scene.start(GAME_MAIN_MENU_SCENE)"));
    }

    /** Comprueba la carga de la clase Boss desde la página. */
    @Test
    void gamePageLoadsBossClass() throws IOException
    {
        String html = Files.readString(INDEX_HTML);
        assertTrue(html.contains("../js/entity/Boss.js"));
    }

    /** Comprueba la publicación HTTP del código y del asset. */
    @Test
    void bossResourcesAreServed() throws IOException, InterruptedException
    {
        HttpResponse<String> script = sendGet("/js/entity/Boss.js");
        HttpResponse<byte[]> image = sendGetBytes("/assets/images/Orc-001.png");

        assertEquals(200, script.statusCode());
        assertTrue(script.body().contains("class Boss extends Entity"));
        assertEquals(200, image.statusCode());
        assertTrue(image.body().length > 0);
    }

    /** Envía una petición GET con cuerpo de texto. */
    private HttpResponse<String> sendGet(String path) throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + port + path)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /** Envía una petición GET con cuerpo binario. */
    private HttpResponse<byte[]> sendGetBytes(String path) throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + port + path)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofByteArray());
    }
}
