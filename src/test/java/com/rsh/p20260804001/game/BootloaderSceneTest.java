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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <h1>
 * Pruebas de la escena de arranque del juego.
 * </h1>
 *
 * <div>
 * Verifica la existencia y el contenido de {@code webcontent/js/scene/BootloaderScene.js}, su
 * integración como primera escena en la configuración del juego y el servicio del asset de la
 * splash screen a través del {@link GameServer}.
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
class BootloaderSceneTest
{
    /** Ruta del fichero de la escena de arranque. */
    private static final Path BOOTLOADER_JS = Path.of("webcontent/js/scene/BootloaderScene.js");

    /** Ruta del fichero de configuración del juego. */
    private static final Path INDEX_JS = Path.of("webcontent/js/index.js");

    /** Ruta del fichero HTML de la página principal. */
    private static final Path INDEX_HTML = Path.of("webcontent/pages/index.html");

    /** Ruta del asset de la splash screen. */
    private static final Path SPLASH_LOGO = Path.of("webcontent/assets/images/SplashScreenLogo.png");

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
     * La escena de arranque y su asset existen.
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
    void bootloaderSceneAndAssetExist()
    {
        assertTrue(Files.isRegularFile(BOOTLOADER_JS), "No existe webcontent/js/scene/BootloaderScene.js");
        assertTrue(Files.isRegularFile(SPLASH_LOGO), "No existe webcontent/assets/images/SplashScreenLogo.png");
    }

    /**
     * <h2>
     * La configuración del juego usa la escena de arranque como primera escena.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la lectura del fichero de configuración.
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
    void indexJsUsesBootloaderAsFirstScene() throws IOException
    {
        String js = Files.readString(INDEX_JS);

        assertTrue(js.contains("scene: ["), "La configuración no declara un array de escenas");
        assertTrue(js.contains("BootloaderScene"), "La escena de arranque no está en la configuración");
        assertFalse(js.contains("scene: {"), "Persiste una configuración de escena anterior");
        assertTrue(js.indexOf("BootloaderScene") > js.indexOf("scene: ["),
                "La escena de arranque no es la primera del array");
    }

    /**
     * <h2>
     * El HTML carga la escena de arranque antes que el script principal.
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
    void indexHtmlLoadsBootloaderBeforeIndexJs() throws IOException
    {
        String html = Files.readString(INDEX_HTML);

        int sceneIndex = html.indexOf("../js/scene/BootloaderScene.js");
        int indexJsIndex = html.indexOf("../js/index.js");

        assertTrue(sceneIndex >= 0, "El HTML no carga la escena de arranque");
        assertTrue(indexJsIndex >= 0, "El HTML no carga el script principal");
        assertTrue(sceneIndex < indexJsIndex, "La escena de arranque debe cargarse antes que index.js");
    }

    /**
     * <h2>
     * La escena incluye los elementos de carga mínimos.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la lectura de la escena.
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
    void bootloaderSceneHasLoadingUiElements() throws IOException
    {
        String scene = Files.readString(BOOTLOADER_JS);

        assertTrue(scene.contains("extends Phaser.Scene"), "La escena no extiende Phaser.Scene");
        assertTrue(scene.contains("Cargando..."), "Falta el texto 'Cargando...'");
        assertTrue(scene.contains("Monospace"), "El texto no usa fuente Monospace");
        assertTrue(scene.contains("12px"), "El texto no usa fuente de 12px");
        assertTrue(scene.contains("200, 7"), "Falta el borde de la barra de progreso (200x7)");
        assertTrue(scene.contains("198"), "Falta la barra de progreso de 198px");
        assertTrue(scene.contains(", 5, 0xffffff"), "Falta la barra de progreso de 5px");
        assertTrue(scene.contains("SplashScreenLogo"), "Falta el asset de la splash screen");
    }

    /**
     * <h2>
     * La escena declara los tiempos de la splash screen y la siguiente escena.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la lectura de la escena.
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
    void bootloaderSceneDeclaresSplashTimingsAndNextScene() throws IOException
    {
        String scene = Files.readString(BOOTLOADER_JS);

        assertTrue(scene.contains("duration: 2500"), "Falta el fade in de 2.5 segundos");
        assertTrue(scene.contains("delayedCall(5000"), "Falta la duración de 5 segundos de la splash");
        assertTrue(scene.contains("nextScene"), "Falta la variable de la siguiente escena");
        assertTrue(scene.contains("pointerdown"), "Falta la interacción por click de la pantalla");
    }

    /**
     * <h2>
     * El asset de la splash screen es un fichero PNG válido.
     * </h2>
     *
     * @throws IOException
     *  Un {@link IOException} si falla la lectura del asset.
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
    void splashLogoIsValidPng() throws IOException
    {
        try (InputStream input = Files.newInputStream(SPLASH_LOGO))
        {
            byte[] header = input.readNBytes(8);
            byte[] pngMagic = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
            assertTrue(java.util.Arrays.equals(header, pngMagic), "El fichero no es un PNG válido");
        }
        assertTrue(SPLASH_LOGO.toFile().length() > 0, "El asset de la splash screen está vacío");
    }

    /**
     * <h2>
     * La escena de arranque y el asset son servidos por el servidor.
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
    void bootloaderResourcesAreServed() throws IOException, InterruptedException
    {
        HttpResponse<String> scene = sendGet("/js/scene/BootloaderScene.js");
        assertEquals(200, scene.statusCode());
        assertTrue(scene.headers().firstValue("Content-Type").orElse("").startsWith("application/javascript"));
        assertTrue(scene.body().contains("BootloaderScene"));

        HttpResponse<String> logo = sendGet("/assets/images/SplashScreenLogo.png");
        assertEquals(200, logo.statusCode());
        assertTrue(logo.headers().firstValue("Content-Type").orElse("").startsWith("image/png"));
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
