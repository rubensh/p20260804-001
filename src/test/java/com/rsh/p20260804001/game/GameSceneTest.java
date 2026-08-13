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
            "Wall-Persp001.png", "Wall-Persp002.png", "Wall-Persp003.png");

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
        assertTrue(scene.contains("GAME_FOV_NEAR_PERSPECTIVE_RADIUS = 1"));
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
