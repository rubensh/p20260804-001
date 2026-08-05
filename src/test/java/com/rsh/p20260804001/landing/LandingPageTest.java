package com.rsh.p20260804001.landing;

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
 * Pruebas de la landing page del proyecto.
 * </h1>
 *
 * <div>
 * Verifica la existencia y el contenido de {@code webcontent/pages/index.html} y de la hoja de
 * estilos {@code webcontent/styles/landing.css}, así como su servicio a través del
 * {@link GameServer}.
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
class LandingPageTest
{
    /** Ruta del fichero HTML de la landing page. */
    private static final Path INDEX_HTML = Path.of("webcontent/pages/index.html");

    /** Ruta de la hoja de estilos de la landing page. */
    private static final Path LANDING_CSS = Path.of("webcontent/styles/landing.css");

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
     * Los recursos de la landing page existen en sus rutas esperadas.
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
    void landingResourcesExist()
    {
        assertTrue(Files.isRegularFile(INDEX_HTML), "No existe webcontent/pages/index.html");
        assertTrue(Files.isRegularFile(LANDING_CSS), "No existe webcontent/styles/landing.css");
    }

    /**
     * <h2>
     * El HTML referencia la hoja de estilos de la landing.
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
    void indexReferencesLandingCss() throws IOException
    {
        String html = Files.readString(INDEX_HTML);

        assertTrue(html.contains("../styles/landing.css"), "El HTML no referencia a landing.css");
    }

    /**
     * <h2>
     * El HTML incluye los elementos esenciales de accesibilidad y estructura.
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
    void indexIncludesAccessibilityEssentials() throws IOException
    {
        String html = Files.readString(INDEX_HTML);

        assertTrue(html.contains("lang=\"es\""), "Falta el atributo lang");
        assertTrue(html.contains("name=\"viewport\""), "Falta la meta viewport");
        assertTrue(html.contains("<title>"), "Falta el elemento title");
        assertTrue(html.contains("class=\"skip-link\""), "Falta el enlace de salto al contenido");
        assertTrue(html.contains("<main"), "Falta el landmark principal");
        assertTrue(html.contains("<nav"), "Falta el landmark de navegación");
        assertEquals(1, countOccurrences(html, "<h1"), "Debe existir exactamente un encabezado h1");
    }

    /**
     * <h2>
     * La landing page es servida por el servidor como HTML.
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
    void landingPageIsServedAsHtml() throws IOException, InterruptedException
    {
        HttpResponse<String> response = sendGet("/pages/index.html");

        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("Content-Type").orElse("").startsWith("text/html"));
        assertTrue(response.body().contains("Buried Dark World"));
        assertTrue(response.body().contains("../styles/landing.css"));
    }

    /**
     * <h2>
     * La hoja de estilos es servida por el servidor como CSS.
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
    void landingStylesheetIsServedAsCss() throws IOException, InterruptedException
    {
        HttpResponse<String> response = sendGet("/styles/landing.css");

        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("Content-Type").orElse("").startsWith("text/css"));
        assertTrue(response.body().contains(":root"));
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
     * Cuenta las apariciones de una subcadena dentro de un texto.
     * </h2>
     *
     * @param text
     *  Un {@link String} texto en el que buscar.
     *
     * @param token
     *  Un {@link String} subcadena a contar.
     *
     * @return
     *  Un {@link Integer} número de apariciones de la subcadena.
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
    private int countOccurrences(String text, String token)
    {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(token, index)) >= 0)
        {
            count++;
            index += token.length();
        }
        return count;
    }
}
