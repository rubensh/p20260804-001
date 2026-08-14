package com.rsh.p20260804001.game;

import com.rsh.p20260804001.server.GameServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <h1>
 * Pruebas de la escena principal del juego.
 * </h1>
 *
 * <div>
 * Verifica la integración de {@code GameScene}, el mapa, el jugador, el HUD, los controles,
 * el campo de visión y los recursos gráficos declarados por la feature 0010.
 * </div>
 *
 * @author
 *  Rubén Santana Hernández
 *  rubensh1980@gmail.com
 *
 * @since
 *  p20260804-001:0.0.1
 *
 * @version
 *  0.0.1
 */
class GameSceneTest
{
    /** Ruta de la escena de juego. */
    private static final Path GAME_SCENE_JS = Path.of("webcontent/js/scene/GameScene.js");

    /** Ruta de la configuración de Phaser. */
    private static final Path INDEX_JS = Path.of("webcontent/js/index.js");

    /** Ruta de la página principal. */
    private static final Path INDEX_HTML = Path.of("webcontent/pages/index.html");

    /** Nombres de los assets requeridos por la escena. */
    private static final List<String> ASSET_NAMES = List.of(
            "Background001.png", "Background002.png",
            "Wall001.png", "Wall002.png", "Wall003.png",
            "Wall-Persp001.png", "Wall-Persp002.png", "Wall-Persp003.png",
            "Stairs-Up.png", "Stairs-Down.png",
            "Goblin-001.png", "Goblin-002.png", "Goblin-003.png");

    /** Cliente HTTP empleado por las pruebas. */
    private HttpClient client;

    /** Puerto real del servidor. */
    private int port;

    /** Servidor bajo prueba. */
    private GameServer server;

    /**
     * Inicializa el servidor sobre un puerto libre.
     *
     * @throws IOException si el servidor no puede arrancar.
     */
    @BeforeEach
    void setUp() throws IOException
    {
        server = new GameServer(0);
        server.init(0);
        port = server.getPort();
        client = HttpClient.newHttpClient();
    }

    /** Finaliza el servidor después de cada prueba. */
    @AfterEach
    void tearDown()
    {
        server.stop();
    }

    /** Comprueba la existencia de la escena. */
    @Test
    void gameSceneFileExists()
    {
        assertTrue(Files.isRegularFile(GAME_SCENE_JS), "No existe GameScene.js");
    }

    /** Comprueba la clase Phaser de la escena. */
    @Test
    void gameSceneExtendsPhaserScene() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("class GameScene extends Phaser.Scene"));
        assertTrue(scene.contains("super('GameScene')"));
    }

    /** Comprueba la precarga de todos los assets. */
    @Test
    void gameScenePreloadsAssets() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("preload()"));
        assertTrue(scene.contains("GAME_ASSETS"));
        for (String asset : ASSET_NAMES)
        {
            assertTrue(scene.contains(asset), "No se precarga " + asset);
        }
    }

    /** Comprueba la creación del laberinto y del jugador. */
    @Test
    void gameSceneCreatesMazeAndPlayer() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("this.maze = new Maze()"));
        assertTrue(scene.contains("this.maze.generateMaze"));
        assertTrue(scene.contains("this.maze.getCenter"));
        assertTrue(scene.contains("this.player = new Player"));
        assertTrue(scene.contains("GAME_PLAYER_LEVEL = 0"));
        assertTrue(scene.contains("GAME_PLAYER_FACING_NORTH = 'N'"));
    }

    /** Comprueba la creación del conjunto de enemigos. */
    @Test
    void gameSceneCreatesEnemies() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("GAME_ENEMY_COUNT = 200"));
        assertTrue(scene.contains("this.maze.generateMaze(GAME_ENEMY_COUNT)"));
        assertTrue(scene.contains("this.enemies = this.maze.enemies"));
    }

    /** Comprueba el combate por proximidad y la retirada de bajas. */
    @Test
    void gameSceneResolvesEnemyCombat() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("resolveCombat()"));
        assertTrue(scene.contains("this.player.attack(enemyAhead)"));
        assertTrue(scene.contains("enemy.attack(this.player)"));
        assertTrue(scene.contains("GAME_ENEMY_ADJACENT_DISTANCE"));
        assertTrue(scene.contains("removeDeadEnemies()"));
        assertTrue(scene.contains("this.maze.enemies = this.enemies"));
    }

    /** Comprueba las reglas de representación de los goblins. */
    @Test
    void gameSceneRendersEnemiesInPerspective() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("GAME_ENEMY_FAR_SCALE = 0.25"));
        assertTrue(scene.contains("GAME_ENEMY_MIDDLE_SCALE = 0.5"));
        assertTrue(scene.contains("GAME_ENEMY_NEAR_SCALE = 1.0"));
        assertTrue(scene.contains("GAME_ENEMY_FAR_VERTICAL_OFFSET = -160"));
        assertTrue(scene.contains("GAME_ENEMY_MIDDLE_VERTICAL_OFFSET = -128"));
        assertTrue(scene.contains("GAME_ENEMY_NEAR_VERTICAL_OFFSET = -64"));
        assertTrue(scene.contains("GAME_ENEMY_FAR_ILLUMINATION = 0.25"));
        assertTrue(scene.contains("GAME_ENEMY_MIDDLE_ILLUMINATION = 0.5"));
        assertTrue(scene.contains("GAME_ENEMY_NEAR_ILLUMINATION = 1.0"));
        assertTrue(scene.contains("drawEnemy(cell"));
        assertTrue(scene.contains("GAME_ENEMY_TEXTURE_PREFIX + enemy.enemyType"));
    }

    /** Comprueba las paredes extremas en perspectiva de profundidad dos. */
    @Test
    void gameSceneRendersMiddleOuterPerspectiveWalls() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("perspectiveDepth === GAME_FOV_MIDDLE_DEPTH"));
        assertTrue(scene.contains("lateral === -GAME_FOV_MIDDLE_PERSPECTIVE_RADIUS"));
        assertTrue(scene.contains("lateral === GAME_FOV_MIDDLE_PERSPECTIVE_RADIUS"));
        assertTrue(scene.contains("GAME_FOV_ALIGN_TO_FIELD_EDGE"));
        assertTrue(scene.contains("GAME_FOV_OUTER_PERSPECTIVE_OFFSET_FACTOR = 0.5"));
        assertTrue(scene.contains("outerWallOffsetScale = GAME_FOV_FAR_SCALE"));
        assertTrue(scene.contains("GAME_FOV_WIDTH * outerWallOffsetScale"));
        assertTrue(scene.contains("* outerWallOffsetFactor"));
        assertTrue(scene.contains("perspectiveWidth / 2 + edgeOffset"));
        assertTrue(scene.contains("GAME_FOV_WIDTH - perspectiveWidth / 2 - edgeOffset"));

        int depthLevel = scene.indexOf("renderDepthLevel(depth");
        int drawEnemy = scene.indexOf("drawEnemy(cell", depthLevel);
        String perspectivePass = scene.substring(depthLevel, drawEnemy);
        assertTrue(perspectivePass.contains("isMiddleOuterWall"));
        assertTrue(perspectivePass.contains("this.drawPerspectiveWall"),
                "Las paredes extremas deben dibujarse en el pase de perspectiva de profundidad dos");
    }

    /** Comprueba las paredes extremas en perspectiva de profundidad uno. */
    @Test
    void gameSceneRendersNearOuterPerspectiveWalls() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("GAME_FOV_NEAR_PERSPECTIVE_RADIUS = 2"));
        assertTrue(scene.contains("perspectiveDepth === GAME_FOV_NEAR_DEPTH"));
        assertTrue(scene.contains("lateral === -GAME_FOV_NEAR_PERSPECTIVE_RADIUS"));
        assertTrue(scene.contains("lateral === GAME_FOV_NEAR_PERSPECTIVE_RADIUS"));
        assertTrue(scene.contains("isNearOuterWall"));
        assertTrue(scene.contains("? GAME_FOV_MIDDLE_SCALE"));
        assertTrue(scene.contains(": GAME_FOV_FAR_SCALE"));
        assertTrue(scene.contains("GAME_FOV_NEAR_OUTER_PERSPECTIVE_OFFSET_FACTOR = 0.25"));
        assertTrue(scene.contains("? GAME_FOV_NEAR_OUTER_PERSPECTIVE_OFFSET_FACTOR"));
        assertTrue(scene.contains(": GAME_FOV_OUTER_PERSPECTIVE_OFFSET_FACTOR"));
        assertTrue(scene.contains("(isMiddleOuterWall || isNearOuterWall)"));
        assertTrue(scene.contains("shiftTowardCenter = true"));
        assertTrue(scene.contains("outerWallOffsetScale,"));
        assertTrue(scene.contains("isMiddleOuterWall"));
        assertTrue(scene.contains("perspectiveWidth / 2 - edgeOffset"));
        assertTrue(scene.contains("GAME_FOV_WIDTH - perspectiveWidth / 2 + edgeOffset"));
    }

    /** Comprueba la transición de Game Over. */
    @Test
    void gameSceneHandlesGameOver() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("GAME_OVER_DELAY_MILLISECONDS = 5000"));
        assertTrue(scene.contains("GAME_OVER_TEXT = 'Game Over'"));
        assertTrue(scene.contains("this.player.health <= GAME_OVER_HEALTH_THRESHOLD"));
        assertTrue(scene.contains("showGameOver()"));
        assertTrue(scene.contains("this.gameOver = true"));
        assertTrue(scene.contains("this.time.delayedCall(GAME_OVER_DELAY_MILLISECONDS"));
        assertTrue(scene.contains("this.scene.start(GAME_MAIN_MENU_SCENE)"));
    }

    /** Comprueba las áreas del HUD. */
    @Test
    void gameSceneDrawsHud() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("GameButtonMenu"));
        assertTrue(scene.contains("drawHealthBar()"));
        assertTrue(scene.contains("drawMinimap()"));
        assertTrue(scene.contains("logGraphics"));
        assertTrue(scene.contains("levelLabel"));
    }

    /** Comprueba las tres profundidades visuales. */
    @Test
    void gameSceneRendersFieldOfViewLevels() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("renderFarLevel()"));
        assertTrue(scene.contains("renderMiddleLevel()"));
        assertTrue(scene.contains("renderNearLevel()"));
        assertTrue(scene.contains("GAME_FOV_FAR_SCALE = 0.2"));
        assertTrue(scene.contains("GAME_FOV_MIDDLE_SCALE = 0.4"));
        assertTrue(scene.contains("GAME_FOV_NEAR_SCALE = 0.8"));
        assertTrue(scene.contains("GAME_FOV_FAR_DARKNESS = 0.5"));
        assertTrue(scene.contains("GAME_FOV_MIDDLE_DARKNESS = 0.25"));
        assertTrue(scene.contains("GAME_FOV_FAR_DEPTH = 3"));
        assertTrue(scene.contains("GAME_FOV_MIDDLE_DEPTH = 2"));
        assertTrue(scene.contains("GAME_FOV_NEAR_DEPTH = 1"));
        assertTrue(scene.contains("GAME_FOV_FAR_VERTICAL_OFFSET = 16"));
        assertTrue(scene.contains("GAME_FOV_MIDDLE_VERTICAL_OFFSET = 8"));
        assertTrue(scene.contains("GAME_FOV_NEAR_VERTICAL_OFFSET = 4"));
        assertTrue(scene.contains("GAME_FOV_FAR_PERSPECTIVE_RADIUS = 2"));
        assertTrue(scene.contains("GAME_FOV_MIDDLE_PERSPECTIVE_RADIUS = 2"));
        assertTrue(scene.contains("GAME_FOV_NEAR_PERSPECTIVE_RADIUS = 2"));
    }

    /** Comprueba la selección alterna de fondos. */
    @Test
    void gameSceneSelectsBackgroundsByParity() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("(x + y) % 2 === 0"));
        assertTrue(scene.contains("'Background001' : 'Background002'"));
    }

    /** Comprueba la selección de paredes mediante módulo tres. */
    @Test
    void gameSceneSelectsWallsByModulo() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("Math.abs(x + y) % 3 + 1"));
        assertTrue(scene.contains("'Wall00' + wallIndex"));
    }

    /** Comprueba las paredes laterales en perspectiva. */
    @Test
    void gameSceneDrawsPerspectiveWalls() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("selectPerspectiveWall"));
        assertTrue(scene.contains("'Wall-Persp00' + wallIndex"));
        assertTrue(scene.contains("setFlipX(true)"));

        int renderDepth = scene.indexOf("renderDepthLevel(depth");
        int nextMethod = scene.indexOf("    drawFrontWall(cell, lateral", renderDepth);
        String renderDepthMethod = scene.substring(renderDepth, nextMethod);
        int perspectivePass = renderDepthMethod.indexOf("this.drawPerspectiveWall");
        int frontPass = renderDepthMethod.indexOf("this.drawFrontWall");
        assertTrue(perspectivePass >= 0 && frontPass > perspectivePass,
                "Las paredes en perspectiva deben dibujarse antes que las frontales");
    }

    /** Comprueba que la capa cercana dibuja las dos paredes en perspectiva. */
    @Test
    void gameSceneDrawsNearPerspectiveWalls() throws IOException
    {
        String scene = readGameScene();
        int nearLevel = scene.indexOf("renderNearLevel() {");
        int nextMethod = scene.indexOf("renderDepthLevel(", nearLevel);
        int endCall = scene.indexOf(");", nextMethod);
        String nearCall = scene.substring(nextMethod, endCall);
        assertTrue(nearCall.contains("GAME_FOV_NEAR_RADIUS"));
        assertTrue(nearCall.contains("GAME_FOV_NEAR_PERSPECTIVE_RADIUS"));
    }

    /** Comprueba el escalado y la alineación superior de las paredes en perspectiva. */
    @Test
    void gameSceneScalesPerspectiveWalls() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("GAME_FOV_PERSPECTIVE_SCALE = 0.5"));
        assertTrue(scene.contains("GAME_FOV_PERSPECTIVE_SOURCE_WIDTH = 128"));
        assertTrue(scene.contains("GAME_FOV_PERSPECTIVE_SOURCE_HEIGHT = 512"));
        assertTrue(scene.contains("wallWidth * sourceWidthRatio * GAME_FOV_PERSPECTIVE_SCALE"));
        assertTrue(scene.contains("wallHeight * sourceHeightRatio * GAME_FOV_PERSPECTIVE_SCALE"));
        assertTrue(scene.contains("frontWallRight + perspectiveWidth / 2"));
        assertTrue(scene.contains("frontWallLeft - perspectiveWidth / 2"));
    }

    /** Comprueba las paredes laterales situadas junto al jugador. */
    @Test
    void gameSceneRendersPlayerLevelSideWalls() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("this.renderPlayerLevel()"));
        assertTrue(scene.contains("GAME_FOV_PLAYER_DEPTH = 0"));
        assertTrue(scene.contains("GAME_FOV_PLAYER_VERTICAL_OFFSET = 4"));
        assertTrue(scene.contains("drawPlayerSideWall(left, true)"));
        assertTrue(scene.contains("drawPlayerSideWall(right, false)"));
        assertTrue(scene.contains("nearWallLeftEdge - perspectiveWidth / 2"));
        assertTrue(scene.contains("nearWallRightEdge + perspectiveWidth / 2"));
        assertTrue(scene.contains("this.fieldOfViewContainer.setMask"));
        assertTrue(scene.contains("drawPlayerFrontSideWall(forwardLeft, true)"));
        assertTrue(scene.contains("drawPlayerFrontSideWall(forwardRight, false)"));

        int playerLevel = scene.indexOf("renderPlayerLevel() {");
        int nextMethod = scene.indexOf("drawPlayerSideWall(cell", playerLevel);
        String playerLevelMethod = scene.substring(playerLevel, nextMethod);
        int frontWalls = playerLevelMethod.indexOf("this.drawPlayerFrontSideWall");
        int perspectiveWalls = playerLevelMethod.indexOf("this.drawPlayerSideWall");
        assertTrue(frontWalls >= 0 && perspectiveWalls > frontWalls,
                "La capa extra debe dibujar primero las paredes frontales");
    }

    /** Comprueba la exclusión específica del campo de visión al mirar al norte. */
    @Test
    void gameSceneExcludesNorthRelativeCell() throws IOException
    {
        String scene = readGameScene();
        String maze = Files.readString(Path.of("webcontent/js/Maze.js"));

        assertTrue(scene.contains("GAME_FOV_NORTH_EXCLUDED_X = 2"));
        assertTrue(scene.contains("GAME_FOV_NORTH_EXCLUDED_Y = -1"));
        assertTrue(scene.contains("this.player.facing === GAME_PLAYER_FACING_NORTH"));
        assertTrue(scene.contains("!this.isExcludedFromFieldOfView(x, y)"));
        assertTrue(scene.contains("this.isExcludedFromFieldOfView(cell.x, cell.y)"));
        int northMaskStart = maze.indexOf("north: [");
        int northMaskEnd = maze.indexOf("south: [", northMaskStart);
        String northMask = maze.substring(northMaskStart, northMaskEnd);
        assertTrue(northMask.contains("[0, 1, 1, 1, 0]"),
                "La máscara norte no distingue correctamente (+1, -1) de (+2, -1)");
    }

    /** Comprueba la representación de escaleras en las tres profundidades. */
    @Test
    void gameSceneRendersStaircases() throws IOException
    {
        String scene = readGameScene();

        assertTrue(scene.contains("GAME_STAIRS_FAR_SCALE = 0.25"));
        assertTrue(scene.contains("GAME_STAIRS_MIDDLE_SCALE = 0.5"));
        assertTrue(scene.contains("GAME_STAIRS_NEAR_SCALE = 1.0"));
        assertTrue(scene.contains("GAME_STAIRS_FAR_VERTICAL_OFFSET = 14"));
        assertTrue(scene.contains("GAME_STAIRS_MIDDLE_VERTICAL_OFFSET = 2"));
        assertTrue(scene.contains("GAME_STAIRS_NEAR_VERTICAL_OFFSET = 0"));
        assertTrue(scene.contains("GAME_STAIRS_FAR_ILLUMINATION = 0.25"));
        assertTrue(scene.contains("GAME_STAIRS_MIDDLE_ILLUMINATION = 0.5"));
        assertTrue(scene.contains("GAME_STAIRS_NEAR_ILLUMINATION = 1.0"));
        assertTrue(scene.contains("staircase.setTint(this.getIlluminationTint(illumination))"));
        assertTrue(scene.contains("room === MAZE_STAIRCASE_UP ? 'Stairs-Up' : 'Stairs-Down'"));
        assertTrue(scene.contains("this.fieldOfViewContainer.setMask"));
    }

    /** Comprueba los símbolos de escaleras visibles en el minimapa. */
    @Test
    void gameSceneDrawsStaircasesOnMinimap() throws IOException
    {
        String scene = readGameScene();

        assertTrue(scene.contains("GAME_MINIMAP_STAIRCASE_DOWN_COLOR = 0xff0000"));
        assertTrue(scene.contains("GAME_MINIMAP_STAIRCASE_UP_COLOR = 0x0000ff"));
        assertTrue(scene.contains("if (visibility[y][x])"));
        assertTrue(scene.contains("this.drawMinimapStaircase(level[y][x]"));
        assertTrue(scene.contains("room === MAZE_STAIRCASE_DOWN"));
        assertTrue(scene.contains("room === MAZE_STAIRCASE_UP"));
        assertTrue(scene.contains("this.minimapGraphics.fillTriangle"));
    }

    /** Comprueba los colores y el jugador del minimapa. */
    @Test
    void gameSceneDrawsMinimap() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("GAME_MINIMAP_HIDDEN_COLOR"));
        assertTrue(scene.contains("GAME_MINIMAP_WALL_COLOR"));
        assertTrue(scene.contains("GAME_MINIMAP_VISITABLE_COLOR"));
        assertTrue(scene.contains("GAME_MINIMAP_PLAYER_COLOR"));
        assertTrue(scene.contains("this.maze.visibility[this.player.level]"));
    }

    /** Comprueba las dimensiones y colores de la barra de salud. */
    @Test
    void gameSceneDrawsHealthBar() throws IOException
    {
        String scene = readGameScene();
        assertTrue(scene.contains("GAME_HEALTH_BORDER_WIDTH = 2"));
        assertTrue(scene.contains("GAME_HEALTH_INNER_MARGIN = 1"));
        assertTrue(scene.contains("GAME_HEALTH_BORDER_COLOR = 0xff0000"));
        assertTrue(scene.contains("GAME_HEALTH_FILL_COLOR = 0x660000"));
        assertTrue(scene.contains("this.player.health / GAME_PLAYER_HEALTH"));
    }

    /** Comprueba la asociación de las teclas de dirección. */
    @Test
    void gameSceneHandlesKeyboardControls() throws IOException
    {
        String scene = readGameScene();
        assertKeyAction(scene, "keydown-UP", "moveForward()");
        assertKeyAction(scene, "keydown-DOWN", "moveBackward()");
        assertKeyAction(scene, "keydown-LEFT", "rotateLeft()");
        assertKeyAction(scene, "keydown-RIGHT", "rotateRight()");
        assertTrue(scene.contains("markVisibleFieldOfView()"));
        assertTrue(scene.contains("drawMinimap()"));
    }

    /** Comprueba que la perspectiva usa las coordenadas cardinales del jugador. */
    @Test
    void gameSceneUsesCorrectCardinalCoordinates() throws IOException
    {
        String scene = readGameScene();

        assertTrue(scene.contains("N: { forwardX: 0, forwardY: -1"),
                "El norte no reduce la coordenada y");
        assertTrue(scene.contains("S: { forwardX: 0, forwardY: 1"),
                "El sur no incrementa la coordenada y");
        assertTrue(scene.contains("E: { forwardX: 1, forwardY: 0"),
                "El este no incrementa la coordenada x");
        assertTrue(scene.contains("W: { forwardX: -1, forwardY: 0"),
                "El oeste no reduce la coordenada x");
    }

    /** Comprueba la posición de GameScene en la configuración. */
    @Test
    void indexJsRegistersGameScene() throws IOException
    {
        String index = Files.readString(INDEX_JS);
        int menu = index.indexOf("MainMenuScene");
        int game = index.indexOf("GameScene");
        int ui = index.indexOf("UITestScene");
        assertTrue(menu >= 0 && game > menu && ui > game, "GameScene no está tras MainMenuScene");
    }

    /** Comprueba la declaración del script en la página. */
    @Test
    void indexHtmlDeclaresGameScene() throws IOException
    {
        String html = Files.readString(INDEX_HTML);
        int game = html.indexOf("../js/scene/GameScene.js");
        int index = html.indexOf("../js/index.js");
        assertTrue(game >= 0 && game < index, "GameScene.js no se carga antes de index.js");
    }

    /** Comprueba que Enemy se carga después de Entity y antes de GameScene. */
    @Test
    void indexHtmlDeclaresEnemy() throws IOException
    {
        String html = Files.readString(INDEX_HTML);
        int entity = html.indexOf("../js/entity/Entity.js");
        int enemy = html.indexOf("../js/entity/Enemy.js");
        int game = html.indexOf("../js/scene/GameScene.js");
        assertTrue(entity >= 0 && enemy > entity && game > enemy,
                "Enemy.js debe cargarse entre Entity.js y GameScene.js");
    }

    /** Comprueba que todos los PNG se sirven y son válidos. */
    @Test
    void gameSceneAssetsAreServed() throws IOException, InterruptedException
    {
        byte[] pngMagic = {(byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a};
        for (String asset : ASSET_NAMES)
        {
            Path path = Path.of("webcontent/assets/images", asset);
            assertTrue(Files.isRegularFile(path), "No existe " + asset);
            try (InputStream input = Files.newInputStream(path))
            {
                assertTrue(java.util.Arrays.equals(pngMagic, input.readNBytes(pngMagic.length)),
                        asset + " no es un PNG válido");
            }
            HttpResponse<byte[]> response = sendGetBytes("/assets/images/" + asset);
            assertEquals(200, response.statusCode());
            assertTrue(response.headers().firstValue("Content-Type").orElse("").startsWith("image/png"));
        }
    }

    /** Comprueba el servicio HTTP de GameScene. */
    @Test
    void gameSceneResourceIsServed() throws IOException, InterruptedException
    {
        HttpResponse<String> response = sendGet("/js/scene/GameScene.js");
        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("Content-Type").orElse("")
                .startsWith("application/javascript"));
        assertTrue(response.body().contains("class GameScene extends Phaser.Scene"));
    }

    /** Lee el código de GameScene. */
    private String readGameScene() throws IOException
    {
        return Files.readString(GAME_SCENE_JS);
    }

    /** Comprueba que una tecla invoca su acción dentro del bloque correspondiente. */
    private void assertKeyAction(String scene, String key, String action)
    {
        int keyPosition = scene.indexOf(key);
        int actionPosition = scene.indexOf(action, keyPosition);
        assertTrue(keyPosition >= 0 && actionPosition > keyPosition,
                "La tecla " + key + " no invoca " + action);
    }

    /** Envía una petición GET de texto. */
    private HttpResponse<String> sendGet(String path) throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /** Envía una petición GET binaria. */
    private HttpResponse<byte[]> sendGetBytes(String path) throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofByteArray());
    }
}
