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
 * Pruebas de la página principal del juego.
 * </h1>
 *
 * <div>
 * Verifica la existencia y el contenido de {@code webcontent/pages/index.html}, de la hoja de
 * estilos {@code webcontent/styles/styles.css}, del script {@code webcontent/js/index.js} y del
 * framework Phaser descargado, así como su servicio a través del {@link GameServer}.
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
class GamePageTest
{
    /** Ruta del fichero HTML de la página principal del juego. */
    private static final Path INDEX_HTML = Path.of("webcontent/pages/index.html");

    /** Ruta de la hoja de estilos de la página principal. */
    private static final Path STYLES_CSS = Path.of("webcontent/styles/styles.css");

    /** Ruta del script de la página principal. */
    private static final Path INDEX_JS = Path.of("webcontent/js/index.js");

    /** Ruta del framework Phaser. */
    private static final Path PHASER_JS = Path.of("webcontent/js/phaser.min.js");

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
     * Los recursos de la página principal existen en sus rutas esperadas.
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
    void gamePageResourcesExist()
    {
        assertTrue(Files.isRegularFile(INDEX_HTML), "No existe webcontent/pages/index.html");
        assertTrue(Files.isRegularFile(STYLES_CSS), "No existe webcontent/styles/styles.css");
        assertTrue(Files.isRegularFile(INDEX_JS), "No existe webcontent/js/index.js");
        assertTrue(Files.isRegularFile(PHASER_JS), "No existe webcontent/js/phaser.min.js");
    }

    /**
     * <h2>
     * El HTML referencia la hoja de estilos, el script y el framework Phaser.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la lectura del fichero HTML.
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
    void indexReferencesStylesScriptAndPhaser() throws IOException
    {
        String html = Files.readString(INDEX_HTML);

        assertTrue(html.contains("../styles/styles.css"), "El HTML no referencia a styles.css");
        assertTrue(html.contains("../js/index.js"), "El HTML no referencia a index.js");
        assertTrue(html.contains("../js/phaser.min.js"), "El HTML no referencia al fichero de Phaser");
    }

    /**
     * <h2>
     * La hoja de estilos define fondos negros para la página y para el canvas.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la lectura de la hoja de estilos.
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
    void stylesheetSetsBlackBackgroundsAndCenteredCanvas() throws IOException
    {
        String css = Files.readString(STYLES_CSS);

        assertTrue(css.contains("background-color: #000000"), "Falta el fondo negro de la página");
        assertTrue(css.contains(".game-container"), "Falta el estilo del contenedor del canvas");
        assertTrue(css.contains("width: 800px"), "El contenedor del canvas no mide 800 px");
        assertTrue(css.contains("align-items: center"), "El canvas no está centrado");
    }

    /**
     * <h2>
     * El script configura Phaser con un canvas de 800 x 600 píxeles.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la lectura del script.
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
    void indexJsConfiguresPhaserCanvas800x600() throws IOException
    {
        String js = Files.readString(INDEX_JS);

        assertTrue(js.contains("new Phaser.Game"), "El script no inicializa Phaser");
        assertTrue(js.contains("width: 800"), "El canvas no mide 800 px de ancho");
        assertTrue(js.contains("height: 600"), "El canvas no mide 600 px de alto");
        assertTrue(js.contains("parent: 'game'"), "El canvas no se monta en el contenedor #game");
        assertTrue(js.contains("'#000000'") || js.contains("#000000"), "El fondo del canvas no es negro");
    }

    /**
     * <h2>
     * El fichero de Phaser descargado es un fichero JavaScript válido.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la lectura del fichero de Phaser.
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
    void phaserFileIsValidJavaScript()
    {
        long size = PHASER_JS.toFile().length();
        assertTrue(size > 0, "El fichero de Phaser está vacío");
    }

    /**
     * <h2>
     * La página principal y sus recursos son servidos por el servidor.
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
    void gamePageAndResourcesAreServed() throws IOException, InterruptedException
    {
        HttpResponse<String> page = sendGet("/pages/index.html");
        assertEquals(200, page.statusCode());
        assertTrue(page.headers().firstValue("Content-Type").orElse("").startsWith("text/html"));
        assertTrue(page.body().contains("../js/phaser.min.js"));

        HttpResponse<String> css = sendGet("/styles/styles.css");
        assertEquals(200, css.statusCode());
        assertTrue(css.headers().firstValue("Content-Type").orElse("").startsWith("text/css"));

        HttpResponse<String> script = sendGet("/js/index.js");
        assertEquals(200, script.statusCode());
        assertTrue(script.headers().firstValue("Content-Type").orElse("").startsWith("application/javascript"));

        HttpResponse<String> phaser = sendGet("/js/phaser.min.js");
        assertEquals(200, phaser.statusCode());
        assertTrue(phaser.headers().firstValue("Content-Type").orElse("").startsWith("application/javascript"));
        assertTrue(phaser.body().length() > 0);
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
