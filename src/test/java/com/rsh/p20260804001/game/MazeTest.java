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

/**
 * <h1>
 * Pruebas de la clase del laberinto.
 * </h1>
 *
 * <div>
 * Verifica la existencia y el contenido de {@code webcontent/js/Maze.js}: las dimensiones por
 * defecto, las codificaciones de las habitaciones, la generación procedural de tipo "drunken miner",
 * los campos de visión y el servicio del recurso a través del {@link GameServer}.
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
class MazeTest
{
    /** Ruta del fichero de la clase del laberinto. */
    private static final Path MAZE_JS = Path.of("webcontent/js/Maze.js");

    /** Cliente HTTP empleado para enviar las peticiones de prueba. */
    private HttpClient client;

    /** Puerto real de escucha del servidor. */
    private int port;

    /** Servidor bajo prueba. */
    private GameServer server;

    /**
     * <h2>
     * Inicializa el entorno de pruebas.
     * </h2>
     *
     * <div>
     * Arranca un {@link GameServer} sobre la carpeta de contenidos del proyecto en un puerto
     * libre.
     * </div>
     *
     * @throws IOException
     *  Un {@link IOException} si falla el arranque del servidor.
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
    @BeforeEach
    void setUp() throws IOException
    {
        server = new GameServer(0);
        server.init(0);
        port = server.getPort();
        client = HttpClient.newHttpClient();
    }

    /**
     * <h2>
     * Finaliza el entorno de pruebas.
     * </h2>
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
    @AfterEach
    void tearDown()
    {
        server.stop();
    }

    /**
     * <h2>
     * El fichero de la clase del laberinto existe.
     * </h2>
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
    @Test
    void mazeFileExists()
    {
        assertTrue(Files.isRegularFile(MAZE_JS), "No existe webcontent/js/Maze.js");
    }

    /**
     * <h2>
     * La clase Maze declara la generación del mapa.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la lectura del fichero.
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
    @Test
    void mazeClassAndGenerateMapDeclared() throws IOException
    {
        String maze = Files.readString(MAZE_JS);

        assertTrue(maze.contains("class Maze"), "No existe la clase Maze");
        assertTrue(maze.contains("generateMap"), "Falta el método generateMap");
    }

    /**
     * <h2>
     * La clase Maze declara las dimensiones por defecto.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la lectura del fichero.
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
    @Test
    void mazeDeclaresDefaultDimensions() throws IOException
    {
        String maze = Files.readString(MAZE_JS);

        assertTrue(maze.contains("MAZE_DEFAULT_WIDTH = 32"), "El ancho por defecto no es 32");
        assertTrue(maze.contains("MAZE_DEFAULT_HEIGHT = 32"), "El alto por defecto no es 32");
        assertTrue(maze.contains("MAZE_DEFAULT_DEPTH = 5"), "La profundidad por defecto no es 5");
    }

    /**
     * <h2>
     * La clase Maze declara las codificaciones de las habitaciones.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la lectura del fichero.
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
    @Test
    void mazeDeclaresRoomEncodings() throws IOException
    {
        String maze = Files.readString(MAZE_JS);

        assertTrue(maze.contains("MAZE_ROOM_EMPTY = 0"), "Falta la codificación de habitación hueca");
        assertTrue(maze.contains("MAZE_ROOM_WALL = 1"), "Falta la codificación de habitación ocupada");
        assertTrue(maze.contains("MAZE_STAIRCASE_DOWN = 2"), "Falta la codificación de escalera hacia abajo");
        assertTrue(maze.contains("MAZE_STAIRCASE_UP = 3"), "Falta la codificación de escalera hacia arriba");
    }

    /**
     * <h2>
     * La clase Maze declara los niveles y la visibilidad.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la lectura del fichero.
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
    @Test
    void mazeDeclaresLevelsAndVisibility() throws IOException
    {
        String maze = Files.readString(MAZE_JS);

        assertTrue(maze.contains("this.levels"), "Falta la propiedad levels");
        assertTrue(maze.contains("this.visibility"), "Falta la propiedad visibility");
    }

    /**
     * <h2>
     * La clase Maze implementa la generación tipo "drunken miner".
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la lectura del fichero.
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
    @Test
    void mazeImplementsDrunkenMinerGeneration() throws IOException
    {
        String maze = Files.readString(MAZE_JS);

        assertTrue(maze.contains("percentVisitable"), "Falta el parámetro del porcentaje de visitables");
        assertTrue(maze.contains("Math.floor(percentVisitable)"),
                "Falta el cálculo del objetivo de celdas visitables");
        assertTrue(maze.contains("isBorderCell"), "Falta la comprobación de celdas frontera");
        assertTrue(maze.contains("findCorridorFrontier"), "Falta la búsqueda de una frontera conectada");
    }

    /** Comprueba la nueva firma de generación con pasos rectos. */
    @Test
    void mazeGenerateMapAcceptsSteps() throws IOException
    {
        String maze = Files.readString(MAZE_JS);

        assertTrue(maze.contains("generateMap(width, height, percentVisitable, steps, currentLevel)"),
                "generateMap no recibe el parámetro steps");
    }

    /** Comprueba el cálculo de pasos y celdas visitables para cada nivel. */
    @Test
    void mazeCalculatesGenerationParametersByLevel() throws IOException
    {
        String maze = Files.readString(MAZE_JS);

        assertTrue(maze.contains("level = currentLevel"));
        assertTrue(maze.contains("(MAZE_BASE_CORRIDOR_LENGTH - level) * MAZE_CORRIDOR_STEP_MULTIPLIER"));
        assertTrue(maze.contains("this.width * this.height * (level + MAZE_LEVEL_NUMBER_OFFSET)"));
        assertTrue(maze.contains("MAZE_VISITABLE_DEPTH_FACTOR = 0.8"));
        assertTrue(maze.contains("/ (MAZE_DEFAULT_DEPTH * MAZE_VISITABLE_DEPTH_FACTOR)"));
        assertTrue(maze.contains("this.generateMap(this.width, this.height, percentVisitable, steps, currentLevel)"));
    }

    /** Comprueba que cada dirección se mantiene durante el número de pasos indicado. */
    @Test
    void mazeWalksStraightForConfiguredSteps() throws IOException
    {
        String maze = Files.readString(MAZE_JS);

        assertTrue(maze.contains("currentStep < steps && visitableCells < targetVisitableCells"));
        assertTrue(maze.contains("current.x + direction.x"));
        assertTrue(maze.contains("current.y + direction.y"));
        assertTrue(maze.contains("matrix[current.y][current.x] === MAZE_ROOM_WALL"));
    }

    /** Comprueba que los tramos rectos no perforan el borde del mapa. */
    @Test
    void mazeStraightWalkPreservesBorders() throws IOException
    {
        String maze = Files.readString(MAZE_JS);

        int stepsLoop = maze.indexOf("currentStep < steps");
        int borderCheck = maze.indexOf("this.isBorderCell(next.x, next.y, width, height)", stepsLoop);
        int markCell = maze.indexOf("markVisitable(current.x, current.y)", borderCheck);
        assertTrue(stepsLoop >= 0 && borderCheck > stepsLoop && markCell > borderCheck,
                "El borde debe comprobarse antes de marcar cada paso como visitable");
    }

    /** Comprueba que un tramo sin cambios fuerza el avance desde una frontera conectada. */
    @Test
    void mazeGenerationGuaranteesProgress() throws IOException
    {
        String maze = Files.readString(MAZE_JS);

        assertTrue(maze.contains("visitableCellsBeforeWalk"));
        assertTrue(maze.contains("this.findCorridorFrontier(matrix, width, height)"));
        assertTrue(maze.contains("current = frontier.current"));
        assertTrue(maze.contains("forcedDirection = frontier.direction"));
        assertTrue(maze.contains("matrix[nextY][nextX] === MAZE_ROOM_WALL"));
    }

    /**
     * <h2>
     * La clase Maze declara los campos de visión.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la lectura del fichero.
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
    @Test
    void mazeDeclaresFieldOfViewMasks() throws IOException
    {
        String maze = Files.readString(MAZE_JS);

        assertTrue(maze.contains("MAZE_VISION_MASKS"), "Faltan las máscaras de campo de visión");
        assertTrue(maze.contains("north"), "Falta el campo de visión al norte");
        assertTrue(maze.contains("south"), "Falta el campo de visión al sur");
        assertTrue(maze.contains("east"), "Falta el campo de visión al este");
        assertTrue(maze.contains("west"), "Falta el campo de visión al oeste");
    }

    /**
     * <h2>
     * La clase Maze inicializa la visibilidad inicial.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la lectura del fichero.
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
    @Test
    void mazeInitializesFieldOfView() throws IOException
    {
        String maze = Files.readString(MAZE_JS);

        assertTrue(maze.contains("applyInitialFieldOfView"), "Falta la inicialización del campo de visión inicial");
        assertTrue(maze.contains("MAZE_VISION_VISIBLE"), "Falta la marca de celda visible");
    }

    /**
     * <h2>
     * La clase Maze es servida por el servidor.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la petición de prueba.
     *
     * @throws InterruptedException
     *  Una {@link InterruptedException} si se interrumpe la petición de prueba.
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
    @Test
    void mazeResourcesAreServed() throws IOException, InterruptedException
    {
        HttpResponse<String> maze = sendGet("/js/Maze.js");
        assertEquals(200, maze.statusCode());
        assertTrue(maze.headers().firstValue("Content-Type").orElse("").startsWith("application/javascript"));
        assertTrue(maze.body().contains("class Maze"));
    }

    /**
     * <h2>
     * Envía una petición HTTP GET.
     * </h2>
     *
     * @param path
     *  Un {@link String} ruta solicitada.
     *
     * @return
     *  Una {@link HttpResponse} respuesta del servidor.
     *
     * @throws IOException
     *  Un {@link IOException} si falla la petición de prueba.
     *
     * @throws InterruptedException
     *  Una {@link InterruptedException} si se interrumpe la petición de prueba.
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
    private HttpResponse<String> sendGet(String path) throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
