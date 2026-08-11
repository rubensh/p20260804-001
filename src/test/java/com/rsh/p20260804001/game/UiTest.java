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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <h1>
 * Pruebas de los elementos de UI del juego.
 * </h1>
 *
 * <div>
 * Verifica la existencia y el contenido de {@code webcontent/js/ui/UIElement.js},
 * {@code webcontent/js/ui/UIButton.js} y {@code webcontent/js/ui/UIPanel.js}, la escena de prueba
 * {@code webcontent/js/scene/UITestScene.js}, su integración en la configuración del juego y el
 * servicio de los recursos a través del {@link GameServer}.
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
class UiTest
{
    /** Ruta del fichero de la clase base de los elementos de UI. */
    private static final Path UI_ELEMENT_JS = Path.of("webcontent/js/ui/UIElement.js");

    /** Ruta del fichero de la clase del botón de UI. */
    private static final Path UI_BUTTON_JS = Path.of("webcontent/js/ui/UIButton.js");

    /** Ruta del fichero de la clase del panel de UI. */
    private static final Path UI_PANEL_JS = Path.of("webcontent/js/ui/UIPanel.js");

    /** Ruta del fichero de la escena de prueba de UI. */
    private static final Path UI_TEST_SCENE_JS = Path.of("webcontent/js/scene/UITestScene.js");

    /** Ruta del fichero de la escena de arranque. */
    private static final Path BOOTLOADER_JS = Path.of("webcontent/js/scene/BootloaderScene.js");

    /** Ruta del fichero de configuración del juego. */
    private static final Path INDEX_JS = Path.of("webcontent/js/index.js");

    /** Ruta del fichero HTML de la página principal. */
    private static final Path INDEX_HTML = Path.of("webcontent/pages/index.html");

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
     * Los ficheros de los elementos de UI y de la escena de prueba existen.
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
    void uiClassesAndTestSceneExist()
    {
        assertTrue(Files.isRegularFile(UI_ELEMENT_JS), "No existe webcontent/js/ui/UIElement.js");
        assertTrue(Files.isRegularFile(UI_BUTTON_JS), "No existe webcontent/js/ui/UIButton.js");
        assertTrue(Files.isRegularFile(UI_PANEL_JS), "No existe webcontent/js/ui/UIPanel.js");
        assertTrue(Files.isRegularFile(UI_TEST_SCENE_JS), "No existe webcontent/js/scene/UITestScene.js");
    }

    /**
     * <h2>
     * La clase base declara sus propiedades.
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
    void uiElementIsBaseClass() throws IOException
    {
        String uiElement = Files.readString(UI_ELEMENT_JS);

        assertTrue(uiElement.contains("class UIElement"), "No se declara la clase UIElement");
        assertTrue(uiElement.contains("this.id"), "Falta la propiedad id");
        assertTrue(uiElement.contains("this.parentElement"), "Falta la propiedad parentElement");
    }

    /**
     * <h2>
     * El botón extiende la clase base y declara sus propiedades.
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
    void uiButtonExtendsUiElementAndHasProperties() throws IOException
    {
        String uiButton = Files.readString(UI_BUTTON_JS);

        assertTrue(uiButton.contains("class UIButton extends UIElement"), "El botón no extiende UIElement");
        assertTrue(uiButton.contains("this.x"), "Falta la propiedad x");
        assertTrue(uiButton.contains("this.y"), "Falta la propiedad y");
        assertTrue(uiButton.contains("this.width"), "Falta la propiedad width");
        assertTrue(uiButton.contains("this.height"), "Falta la propiedad height");
        assertTrue(uiButton.contains("this.backgroundColor"), "Falta la propiedad backgroundColor");
        assertTrue(uiButton.contains("this.borderColor"), "Falta la propiedad borderColor");
        assertTrue(uiButton.contains("this.textColor"), "Falta la propiedad textColor");
        assertTrue(uiButton.contains("this.text"), "Falta la propiedad text");
        assertTrue(uiButton.contains("this.textSize"), "Falta la propiedad textSize");
        assertTrue(uiButton.contains("this.fontName"), "Falta la propiedad fontName");
        assertTrue(uiButton.contains("this.enabled"), "Falta la propiedad enabled");
        assertTrue(uiButton.contains("this.visible"), "Falta la propiedad visible");
        assertTrue(uiButton.contains("this.onClickFunction"), "Falta la propiedad onClickFunction");
    }

    /**
     * <h2>
     * El botón gestiona el estado activo, el resaltado y el clic.
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
    void uiButtonHandlesStateAndEvents() throws IOException
    {
        String uiButton = Files.readString(UI_BUTTON_JS);

        assertTrue(uiButton.contains("setInteractive"), "Falta la activación de los eventos del botón");
        assertTrue(uiButton.contains("disableInteractive"), "Falta la desactivación de los eventos del botón");
        assertTrue(uiButton.contains("pointerover"), "Falta el resaltado al pasar el cursor");
        assertTrue(uiButton.contains("pointerup"), "Falta la interacción de clic");
        assertTrue(uiButton.contains("this.onClickFunction()"), "Falta la ejecución del clic");
        assertTrue(uiButton.contains("toGrayscale"), "Falta el estado de escala de grises");
    }

    /**
     * <h2>
     * El área de interacción del botón compensa el origen centrado del contenedor de Phaser.
     * </h2>
     *
     * <div>
     * El contenedor de Phaser fija su origen en (0.5, 0.5), de modo que su área de interacción
     * queda centrada en la posición del contenedor. Para que los eventos respondan sobre el
     * área visual del botón, el rectángulo de interacción debe desplazarse media anchura y media
     * altura. Sin esta compensación, los eventos responden en coordenadas desplazadas respecto
     * al contenedor.
     * </div>
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
    void uiButtonHitAreaCompensatesContainerOrigin() throws IOException
    {
        String uiButton = Files.readString(UI_BUTTON_JS);

        assertTrue(uiButton.contains("new Phaser.Geom.Rectangle(originX, originY, this.width, this.height)"),
                "El área de interacción no compensa el origen centrado del contenedor");
    }

    /**
     * <h2>
     * El panel extiende la clase base y declara sus propiedades.
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
    void uiPanelExtendsUiElementAndHasProperties() throws IOException
    {
        String uiPanel = Files.readString(UI_PANEL_JS);

        assertTrue(uiPanel.contains("class UIPanel extends UIElement"), "El panel no extiende UIElement");
        assertTrue(uiPanel.contains("this.x"), "Falta la propiedad x");
        assertTrue(uiPanel.contains("this.y"), "Falta la propiedad y");
        assertTrue(uiPanel.contains("this.width"), "Falta la propiedad width");
        assertTrue(uiPanel.contains("this.height"), "Falta la propiedad height");
        assertTrue(uiPanel.contains("this.backgroundColor"), "Falta la propiedad backgroundColor");
        assertTrue(uiPanel.contains("this.borderColor"), "Falta la propiedad borderColor");
        assertTrue(uiPanel.contains("this.enabled"), "Falta la propiedad enabled");
        assertTrue(uiPanel.contains("this.visible"), "Falta la propiedad visible");
        assertTrue(uiPanel.contains("this.elements"), "Falta la propiedad elements");
    }

    /**
     * <h2>
     * Los elementos de UI no usan una API no soportada por el contenedor de Phaser.
     * </h2>
     *
     * <div>
     * El contenedor de Phaser no dispone del método {@code setOrigin}. Su uso provoca el error
     * "this.container.setOrigin is not a function" en tiempo de ejecución.
     * </div>
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
    void uiClassesAvoidUnsupportedContainerApi() throws IOException
    {
        String uiButton = Files.readString(UI_BUTTON_JS);
        String uiPanel = Files.readString(UI_PANEL_JS);

        assertFalse(uiButton.contains("container.setOrigin"), "El botón usa setOrigin, no soportado por el contenedor de Phaser");
        assertFalse(uiPanel.contains("container.setOrigin"), "El panel usa setOrigin, no soportado por el contenedor de Phaser");
    }

    /**
     * <h2>
     * La escena de prueba declara el panel y el botón de prueba.
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
    void uiTestSceneDeclaresTestUi() throws IOException
    {
        String scene = Files.readString(UI_TEST_SCENE_JS);

        assertTrue(scene.contains("extends Phaser.Scene"), "La escena no extiende Phaser.Scene");
        assertTrue(scene.contains("testPanel001"), "Falta el panel de prueba testPanel001");
        assertTrue(scene.contains("testButton001"), "Falta el botón de prueba testButton001");
        assertTrue(scene.contains("addElement(testButton)"), "El botón de prueba no está dentro de su panel");
        assertTrue(scene.contains("rgba(0.5, 0.5, 0.5, 1.0)"), "Falta el color del panel de prueba");
        assertTrue(scene.contains("rgba(0.75, 0.50, 0.25, 1.0)"), "Falta el color del botón de prueba");
        assertTrue(scene.contains("Monospace"), "El botón no usa fuente Monospace");
        assertTrue(scene.contains("Prueba"), "Falta el texto del botón");
        assertTrue(scene.contains("setTimeout"), "Falta la animación del panel con setTimeout");
    }

    /**
     * <h2>
     * La escena de prueba declara el botón de vuelta al menú principal.
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
    void uiTestSceneHasBackButton() throws IOException
    {
        String scene = Files.readString(UI_TEST_SCENE_JS);

        assertTrue(scene.contains("testButtonBack"), "Falta el botón de vuelta testButtonBack");
        assertTrue(scene.contains("Volver al menú principal"), "Falta el texto del botón de vuelta");
        assertTrue(scene.contains("scene.start('MainMenuScene')"), "El botón de vuelta no da paso a MainMenuScene");
    }

    /**
     * <h2>
     * La configuración del juego registra la escena de prueba tras la de arranque.
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
    void indexJsRegistersUiTestSceneAfterBootloader() throws IOException
    {
        String js = Files.readString(INDEX_JS);

        assertTrue(js.contains("BootloaderScene"), "La escena de arranque no está en la configuración");
        assertTrue(js.contains("UITestScene"), "La escena de prueba de UI no está en la configuración");
        assertTrue(js.indexOf("UITestScene") > js.indexOf("BootloaderScene"),
                "La escena de prueba de UI no está tras la de arranque");
    }

    /**
     * <h2>
     * El HTML carga los scripts de UI antes del script principal.
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
    void indexHtmlLoadsUiScriptsBeforeIndexJs() throws IOException
    {
        String html = Files.readString(INDEX_HTML);

        int uiElementIndex = html.indexOf("../js/ui/UIElement.js");
        int uiButtonIndex = html.indexOf("../js/ui/UIButton.js");
        int uiPanelIndex = html.indexOf("../js/ui/UIPanel.js");
        int sceneIndex = html.indexOf("../js/scene/UITestScene.js");
        int indexJsIndex = html.indexOf("../js/index.js");

        assertTrue(uiElementIndex >= 0, "El HTML no carga UIElement.js");
        assertTrue(uiButtonIndex >= 0, "El HTML no carga UIButton.js");
        assertTrue(uiPanelIndex >= 0, "El HTML no carga UIPanel.js");
        assertTrue(sceneIndex >= 0, "El HTML no carga UITestScene.js");
        assertTrue(indexJsIndex >= 0, "El HTML no carga el script principal");
        assertTrue(uiElementIndex < indexJsIndex, "UIElement.js debe cargarse antes que index.js");
        assertTrue(uiButtonIndex < indexJsIndex, "UIButton.js debe cargarse antes que index.js");
        assertTrue(uiPanelIndex < indexJsIndex, "UIPanel.js debe cargarse antes que index.js");
        assertTrue(sceneIndex < indexJsIndex, "UITestScene.js debe cargarse antes que index.js");
    }

    /**
     * <h2>
     * Los recursos de UI son servidos por el servidor.
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
    void uiResourcesAreServed() throws IOException, InterruptedException
    {
        HttpResponse<String> uiElement = sendGet("/js/ui/UIElement.js");
        assertEquals(200, uiElement.statusCode());
        assertTrue(uiElement.headers().firstValue("Content-Type").orElse("").startsWith("application/javascript"));
        assertTrue(uiElement.body().contains("class UIElement"));

        HttpResponse<String> uiButton = sendGet("/js/ui/UIButton.js");
        assertEquals(200, uiButton.statusCode());
        assertTrue(uiButton.headers().firstValue("Content-Type").orElse("").startsWith("application/javascript"));
        assertTrue(uiButton.body().contains("class UIButton"));

        HttpResponse<String> uiPanel = sendGet("/js/ui/UIPanel.js");
        assertEquals(200, uiPanel.statusCode());
        assertTrue(uiPanel.headers().firstValue("Content-Type").orElse("").startsWith("application/javascript"));
        assertTrue(uiPanel.body().contains("class UIPanel"));

        HttpResponse<String> scene = sendGet("/js/scene/UITestScene.js");
        assertEquals(200, scene.statusCode());
        assertTrue(scene.headers().firstValue("Content-Type").orElse("").startsWith("application/javascript"));
        assertTrue(scene.body().contains("class UITestScene"));
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
