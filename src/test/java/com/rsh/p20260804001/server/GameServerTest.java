package com.rsh.p20260804001.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <h1>
 * Pruebas de la clase GameServer.
 * </h1>
 *
 * <div>
 * Verifica el arranque en segundo plano, la atención exclusiva de peticiones HTTP GET y el
 * aislamiento de los contenidos servibles bajo la carpeta raíz.
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
class GameServerTest
{
    /** Cliente HTTP empleado para enviar las peticiones de prueba. */
    private HttpClient client;

    /** Puerto real de escucha del servidor. */
    private int port;

    /** Servidor bajo prueba. */
    private GameServer server;

    /** Carpeta raíz temporal desde la que se sirven los recursos de prueba. */
    private Path webRoot;

    /**
     * <h2>
     * Inicializa el entorno de pruebas.
     * </h2>
     *
     * <div>
     * Crea una carpeta temporal de recursos e inicia un servidor en un puerto libre.
     * </div>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la creación de la carpeta temporal o el arranque.
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
        webRoot = Files.createTempDirectory("game-server-test");
        server = new GameServer(0, webRoot);
        server.init(0);
        port = server.getPort();
        client = HttpClient.newHttpClient();
    }

    /**
     * <h2>
     * Finaliza el entorno de pruebas.
     * </h2>
     *
     * <div>
     * Detiene el servidor y elimina la carpeta temporal de recursos.
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
    @AfterEach
    void tearDown()
    {
        server.stop();
        deleteRecursively(webRoot);
    }

    /**
     * <h2>
     * El servidor queda en modo escucha tras inicializarse.
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
    void initStartsBackgroundListening()
    {
        assertTrue(server.isRunning());
        assertTrue(port > 0);
    }

    /**
     * <h2>
     * Una petición GET de un recurso existente devuelve su contenido.
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
    void getExistingFileReturnsContentAndContentType() throws IOException, InterruptedException
    {
        Files.writeString(webRoot.resolve("index.html"), "<h1>Hola</h1>", StandardCharsets.UTF_8);

        HttpResponse<String> response = sendGet("/index.html");

        assertEquals(200, response.statusCode());
        assertEquals("<h1>Hola</h1>", response.body());
        assertTrue(response.headers().firstValue("Content-Type").orElse("").startsWith("text/html"));
    }

    /**
     * <h2>
     * Una petición GET de un recurso de una subcarpeta se sirve correctamente.
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
    void getFileInSubfolderIsServed() throws IOException, InterruptedException
    {
        Files.createDirectories(webRoot.resolve("assets/img"));
        Files.writeString(webRoot.resolve("assets/img/logo.png"), "imagen-falsa", StandardCharsets.UTF_8);

        HttpResponse<String> response = sendGet("/assets/img/logo.png");

        assertEquals(200, response.statusCode());
        assertEquals("imagen-falsa", response.body());
        assertTrue(response.headers().firstValue("Content-Type").orElse("").startsWith("image/png"));
    }

    /**
     * <h2>
     * Una petición GET de un recurso inexistente devuelve 404.
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
    void getMissingFileReturnsNotFound() throws IOException, InterruptedException
    {
        HttpResponse<String> response = sendGet("/no-existe.html");

        assertEquals(404, response.statusCode());
    }

    /**
     * <h2>
     * Un intento de salir de la carpeta raíz mediante path traversal es rechazado.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la petición de prueba.
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
    void pathTraversalIsRejected() throws IOException
    {
        String response = rawRequest("GET /../pom.xml HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n");

        assertTrue(response.startsWith("HTTP/1.1 403"), response);
    }

    /**
     * <h2>
     * Una ruta absoluta de sistema no se sirve desde la carpeta raíz.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la petición de prueba.
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
    void absolutePathOutsideWebRootIsNotServed() throws IOException
    {
        String response = rawRequest("GET /etc/passwd HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n");

        assertTrue(response.startsWith("HTTP/1.1 404"), response);
    }

    /**
     * <h2>
     * Los métodos HTTP distintos de GET son descartados.
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
    void nonGetMethodsAreDiscarded() throws IOException, InterruptedException
    {
        Files.writeString(webRoot.resolve("index.html"), "<h1>Hola</h1>", StandardCharsets.UTF_8);

        assertEquals(405, send("POST", "/index.html").statusCode());
        assertEquals(405, send("PUT", "/index.html").statusCode());
        assertEquals(405, send("DELETE", "/index.html").statusCode());
    }

    /**
     * <h2>
     * Una petición GET con query string sirve el recurso ignorando la query.
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
    void getWithQueryStringServesResource() throws IOException, InterruptedException
    {
        Files.writeString(webRoot.resolve("index.html"), "<h1>Hola</h1>", StandardCharsets.UTF_8);

        HttpResponse<String> response = sendGet("/index.html?v=1&x=2");

        assertEquals(200, response.statusCode());
        assertEquals("<h1>Hola</h1>", response.body());
    }

    /**
     * <h2>
     * Una petición GET con ruta codificada sirve el recurso decodificado.
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
    void getWithUrlEncodedPathServesResource() throws IOException, InterruptedException
    {
        Files.writeString(webRoot.resolve("mi pagina.html"), "contenido", StandardCharsets.UTF_8);

        HttpResponse<String> response = sendGet("/mi%20pagina.html");

        assertEquals(200, response.statusCode());
        assertEquals("contenido", response.body());
    }

    /**
     * <h2>
     * Una petición malformada devuelve 400.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la petición de prueba.
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
    void malformedRequestReturnsBadRequest() throws IOException
    {
        String response = rawRequest("NOT-A-REQUEST\r\n\r\n");

        assertTrue(response.startsWith("HTTP/1.1 400"), response);
    }

    /**
     * <h2>
     * Inicializar un servidor ya en escucha lanza una excepción.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la petición de prueba.
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
    void initTwiceThrowsIllegalStateException() throws IOException
    {
        // lambda que reutiliza el servidor ya inicializado para provocar la excepción
        assertThrows(IllegalStateException.class, () -> server.init(port));
    }

    /**
     * <h2>
     * Detener el servidor finaliza la escucha y rechaza nuevas conexiones.
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
    void stopEndsListeningAndRejectsNewConnections()
    {
        server.stop();

        assertFalse(server.isRunning());
        // lambda que abre una conexión sobre el puerto ya liberado para verificar el cierre
        assertThrows(IOException.class, () -> new Socket("localhost", port));
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
     * Envía una petición HTTP con el método indicado.
     * </h2>
     *
     * @param method
     *  Un {@link String} método HTTP a emplear.
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
    private HttpResponse<String> send(String method, String path) throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path))
                .method(method, HttpRequest.BodyPublishers.noBody())
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * <h2>
     * Envía una petición HTTP en crudo sobre un socket.
     * </h2>
     *
     * <div>
     * Permite enviar peticiones malformadas o rutas de escape que el cliente HTTP normaliza.
     * </div>
     *
     * @param raw
     *  Un {@link String} petición HTTP completa en crudo.
     *
     * @return
     *  Un {@link String} respuesta completa del servidor.
     *
     * @throws IOException
     *  Un {@link IOException} si falla la comunicación con el servidor.
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
    private String rawRequest(String raw) throws IOException
    {
        try (Socket socket = new Socket("localhost", port))
        {
            OutputStream output = socket.getOutputStream();
            output.write(raw.getBytes(StandardCharsets.US_ASCII));
            output.flush();
            return new String(socket.getInputStream().readAllBytes(), StandardCharsets.ISO_8859_1);
        }
    }

    /**
     * <h2>
     * Elimina de forma recursiva una carpeta.
     * </h2>
     *
     * @param root
     *  Una {@link Path} carpeta a eliminar.
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
    private void deleteRecursively(Path root)
    {
        try (var paths = Files.walk(root))
        {
            // se recorre el árbol en orden inverso para eliminar primero los hijos
            for (Path path : paths.sorted(Comparator.reverseOrder()).toList())
            {
                Files.deleteIfExists(path);
            }
        }
        catch (IOException e)
        {
            // error de limpieza ignorado
        }
    }
}
