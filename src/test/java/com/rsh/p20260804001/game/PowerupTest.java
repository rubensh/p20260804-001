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

/** Pruebas de las pociones de salud de la feature 0015. */
class PowerupTest
{
    /** Ruta de la entidad poción. */
    private static final Path POTION_JS = Path.of("webcontent/js/entity/Potion.js");

    /** Ruta del laberinto. */
    private static final Path MAZE_JS = Path.of("webcontent/js/Maze.js");

    /** Ruta de la escena de juego. */
    private static final Path GAME_SCENE_JS = Path.of("webcontent/js/scene/GameScene.js");

    /** Ruta de la página del juego. */
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

    /** Comprueba la entidad consumible y la restauración de salud. */
    @Test
    void potionIsASingleUseHealthPowerup() throws IOException
    {
        String potion = Files.readString(POTION_JS);
        String player = Files.readString(Path.of("webcontent/js/entity/Player.js"));

        assertTrue(potion.contains("class Potion extends Entity"));
        assertTrue(potion.contains("this.consumed = false"));
        assertTrue(potion.contains("consume(player)"));
        assertTrue(potion.contains("player.restoreHealth()"));
        assertTrue(player.contains("this.maxHealth = health"));
        assertTrue(player.contains("this.health = this.maxHealth"));
    }

    /** Comprueba la generación de cinco pociones por nivel. */
    @Test
    void mazeGeneratesFivePotionsPerLevel() throws IOException
    {
        String maze = Files.readString(MAZE_JS);

        assertTrue(maze.contains("MAZE_POTIONS_PER_LEVEL = 5"));
        assertTrue(maze.contains("this.potions = []"));
        assertTrue(maze.contains("generatePotionsForLevel(matrix, currentLevel)"));
        assertTrue(maze.contains("matrix[y][x] === MAZE_ROOM_EMPTY"));
        assertTrue(maze.contains("!this.isOccupiedByEnemy(currentLevel, x, y)"));
        assertTrue(maze.contains("candidates.splice(candidateIndex"));
    }

    /** Comprueba el consumo y la retirada de la poción recogida. */
    @Test
    void gameSceneConsumesPotionAtPlayerPosition() throws IOException
    {
        String scene = Files.readString(GAME_SCENE_JS);

        assertTrue(scene.contains("consumePotionAtPlayerPosition()"));
        assertTrue(scene.contains("this.findPotionAt(this.player.x, this.player.y)"));
        assertTrue(scene.contains("potion.consume(this.player)"));
        assertTrue(scene.contains("this.maze.potions = this.potions"));
    }

    /** Comprueba la precarga y la posición visual solicitadas. */
    @Test
    void gameSceneRendersPotionBelowViewCenter() throws IOException
    {
        String scene = Files.readString(GAME_SCENE_JS);

        assertTrue(scene.contains("Potion001.png"));
        assertTrue(scene.contains("GAME_POTION_VERTICAL_OFFSET = 30"));
        assertTrue(scene.contains("GAME_FOV_HEIGHT / GAME_CENTER_DIVISOR"
                + " + GAME_POTION_VERTICAL_OFFSET"));
        assertTrue(scene.contains("drawPotion(cell"));
    }

    /** Comprueba que la página carga la clase Potion. */
    @Test
    void gamePageLoadsPotionClass() throws IOException
    {
        String html = Files.readString(INDEX_HTML);
        assertTrue(html.contains("../js/entity/Potion.js"));
    }

    /** Comprueba la publicación HTTP del código y del asset. */
    @Test
    void potionResourcesAreServed() throws IOException, InterruptedException
    {
        HttpResponse<String> script = sendGet("/js/entity/Potion.js");
        HttpResponse<byte[]> image = sendGetBytes("/assets/images/Potion001.png");

        assertEquals(200, script.statusCode());
        assertTrue(script.body().contains("class Potion extends Entity"));
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
