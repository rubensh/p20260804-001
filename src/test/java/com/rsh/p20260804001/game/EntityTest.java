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
 * Pruebas de las entidades del juego.
 * </h1>
 *
 * <div>
 * Verifica la existencia y el contenido de {@code webcontent/js/entity/Entity.js} (entidad base) y
 * de {@code webcontent/js/entity/Player.js} (entidad jugador): propiedades, métodos de combate,
 * movimiento, giros y el servicio de los recursos a través del {@link GameServer}.
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
class EntityTest
{
    /** Ruta del fichero de la entidad base. */
    private static final Path ENTITY_JS = Path.of("webcontent/js/entity/Entity.js");

    /** Ruta del fichero de la entidad jugador. */
    private static final Path PLAYER_JS = Path.of("webcontent/js/entity/Player.js");

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
     * El fichero de la entidad base existe.
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
    void entityFileExists()
    {
        assertTrue(Files.isRegularFile(ENTITY_JS), "No existe webcontent/js/entity/Entity.js");
    }

    /**
     * <h2>
     * La clase Entity declara el estado base.
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
    void entityClassDeclaresBaseState() throws IOException
    {
        String entity = Files.readString(ENTITY_JS);

        assertTrue(entity.contains("class Entity"), "No existe la clase Entity");
        assertTrue(entity.contains("constructor(id, x, y, level)"), "Falta el constructor de la entidad");
        assertTrue(entity.contains("this.id = id;"), "Falta la propiedad id");
        assertTrue(entity.contains("this.x = x;"), "Falta la propiedad x");
        assertTrue(entity.contains("this.y = y;"), "Falta la propiedad y");
        assertTrue(entity.contains("this.level = level;"), "Falta la propiedad level");
    }

    /**
     * <h2>
     * El fichero de la entidad jugador existe.
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
    void playerFileExists()
    {
        assertTrue(Files.isRegularFile(PLAYER_JS), "No existe webcontent/js/entity/Player.js");
    }

    /**
     * <h2>
     * La clase Player extiende de Entity.
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
    void playerClassExtendsEntity() throws IOException
    {
        String player = Files.readString(PLAYER_JS);

        assertTrue(player.contains("class Player extends Entity"), "La clase Player no extiende de Entity");
        assertTrue(player.contains("super(id, x, y, level)"), "Falta la llamada al constructor de Entity");
    }

    /**
     * <h2>
     * La clase Player declara sus propiedades.
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
    void playerDeclaresProperties() throws IOException
    {
        String player = Files.readString(PLAYER_JS);

        assertTrue(player.contains("this.maze = maze;"), "Falta la propiedad maze");
        assertTrue(player.contains("this.facing = facing;"), "Falta la propiedad facing");
        assertTrue(player.contains("this.health = health;"), "Falta la propiedad health");
        assertTrue(player.contains("this.minDamage = minDamage;"), "Falta la propiedad minDamage");
        assertTrue(player.contains("this.maxDamage = maxDamage;"), "Falta la propiedad maxDamage");
        assertTrue(player.contains("this.minDefense = minDefense;"), "Falta la propiedad minDefense");
        assertTrue(player.contains("this.maxDefense = maxDefense;"), "Falta la propiedad maxDefense");
    }

    /**
     * <h2>
     * La clase Player declara sus métodos.
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
    void playerDeclaresMethods() throws IOException
    {
        String player = Files.readString(PLAYER_JS);

        assertTrue(player.contains("attack(entity)"), "Falta el método attack");
        assertTrue(player.contains("defend(damage)"), "Falta el método defend");
        assertTrue(player.contains("moveForward()"), "Falta el método moveForward");
        assertTrue(player.contains("moveBackward()"), "Falta el método moveBackward");
        assertTrue(player.contains("rotateLeft()"), "Falta el método rotateLeft");
        assertTrue(player.contains("rotateRight()"), "Falta el método rotateRight");
    }

    /**
     * <h2>
     * El método attack aplica el daño a la entidad objetivo.
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
    void playerAttackAppliesDamage() throws IOException
    {
        String player = Files.readString(PLAYER_JS);

        assertTrue(player.contains("getRandomBetween(this.minDamage, this.maxDamage)"),
                "attack no genera el daño entre minDamage y maxDamage");
        assertTrue(player.contains("entity.defend(damage)"), "attack no delega en defend de la entidad objetivo");
    }

    /**
     * <h2>
     * El método defend resta la diferencia positiva a la salud.
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
    void playerDefendSubtractsPositiveDifference() throws IOException
    {
        String player = Files.readString(PLAYER_JS);

        assertTrue(player.contains("getRandomBetween(this.minDefense, this.maxDefense)"),
                "defend no genera la defensa entre minDefense y maxDefense");
        assertTrue(player.contains("const difference = damage - defense;"), "defend no calcula la diferencia");
        assertTrue(player.contains("if (difference > 0)"), "defend no comprueba la diferencia positiva");
        assertTrue(player.contains("Math.max(0, this.health - difference)"),
                "defend no resta la diferencia a la salud sin superar 0");
    }

    /**
     * <h2>
     * Los movimientos comprueban que la celda destino sea visitable.
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
    void playerMovementChecksVisitableCell() throws IOException
    {
        String player = Files.readString(PLAYER_JS);

        assertTrue(player.contains("isVisitable(target.x, target.y)"),
                "Los movimientos no comprueban la celda destino");
        assertTrue(player.contains("this.maze.levels[this.level]"),
                "La comprobación de visitable no consulta el nivel actual del laberinto");
        assertTrue(player.contains("MAZE_ROOM_WALL"), "La comprobación de visitable no usa la codificación de muro");
    }

    /**
     * <h2>
     * El método moveForward usa las direcciones especificadas.
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
    void playerMoveForwardDirections() throws IOException
    {
        String player = Files.readString(PLAYER_JS);
        String forward = player.substring(player.indexOf("getForwardCell() {"), player.indexOf("getBackwardCell() {"));

        assertOrdered(forward, "this.facing === 'N'", "target.y--;");
        assertOrdered(forward, "this.facing === 'S'", "target.y++;");
        assertOrdered(forward, "this.facing === 'E'", "target.x--;");
        assertOrdered(forward, "this.facing === 'W'", "target.x++;");
    }

    /**
     * <h2>
     * El método moveBackward usa las direcciones especificadas.
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
    void playerMoveBackwardDirections() throws IOException
    {
        String player = Files.readString(PLAYER_JS);
        String backward = player.substring(player.indexOf("getBackwardCell() {"), player.indexOf("getRandomBetween(min, max) {"));

        assertOrdered(backward, "this.facing === 'N'", "target.y++;");
        assertOrdered(backward, "this.facing === 'S'", "target.y--;");
        assertOrdered(backward, "this.facing === 'E'", "target.x++;");
        assertOrdered(backward, "this.facing === 'W'", "target.x--;");
    }

    /**
     * <h2>
     * El método rotateLeft usa los giros especificados.
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
    void playerRotateLeftDirections() throws IOException
    {
        String player = Files.readString(PLAYER_JS);
        String rotateLeft = player.substring(player.indexOf("rotateLeft() {"), player.indexOf("rotateRight() {"));

        assertOrdered(rotateLeft, "this.facing === 'N'", "this.facing = 'W';");
        assertOrdered(rotateLeft, "this.facing === 'S'", "this.facing = 'E';");
        assertOrdered(rotateLeft, "this.facing === 'E'", "this.facing = 'N';");
        assertOrdered(rotateLeft, "this.facing === 'W'", "this.facing = 'S';");
    }

    /**
     * <h2>
     * El método rotateRight usa los giros especificados.
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
    void playerRotateRightDirections() throws IOException
    {
        String player = Files.readString(PLAYER_JS);
        String rotateRight = player.substring(player.indexOf("rotateRight() {"), player.indexOf("getForwardCell() {"));

        assertOrdered(rotateRight, "this.facing === 'N'", "this.facing = 'E';");
        assertOrdered(rotateRight, "this.facing === 'S'", "this.facing = 'W';");
        assertOrdered(rotateRight, "this.facing === 'E'", "this.facing = 'S';");
        assertOrdered(rotateRight, "this.facing === 'W'", "this.facing = 'N';");
    }

    /**
     * <h2>
     * La entidad base es servida por el servidor.
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
    void entityResourcesAreServed() throws IOException, InterruptedException
    {
        HttpResponse<String> entity = sendGet("/js/entity/Entity.js");
        assertEquals(200, entity.statusCode());
        assertTrue(entity.headers().firstValue("Content-Type").orElse("").startsWith("application/javascript"));
        assertTrue(entity.body().contains("class Entity"));
    }

    /**
     * <h2>
     * La entidad jugador es servida por el servidor.
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
    void playerResourcesAreServed() throws IOException, InterruptedException
    {
        HttpResponse<String> player = sendGet("/js/entity/Player.js");
        assertEquals(200, player.statusCode());
        assertTrue(player.headers().firstValue("Content-Type").orElse("").startsWith("application/javascript"));
        assertTrue(player.body().contains("class Player extends Entity"));
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

    /**
     * <h2>
     * Comprueba el orden de dos fragmentos dentro de un bloque.
     * </h2>
     *
     * @param block
     *  Un {@link String} bloque de código donde buscar.
     *
     * @param first
     *  Un {@link String} primer fragmento esperado.
     *
     * @param second
     *  Un {@link String} segundo fragmento esperado.
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
    private void assertOrdered(String block, String first, String second)
    {
        int firstPosition = block.indexOf(first);
        int secondPosition = block.indexOf(second);

        assertTrue(firstPosition != -1, "No se encuentra el fragmento: " + first);
        assertTrue(secondPosition != -1, "No se encuentra el fragmento: " + second);
        assertTrue(firstPosition < secondPosition, "El fragmento " + second + " precede a " + first);
    }
}
