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
 * Pruebas de la escena del menú principal del juego.
 * </h1>
 *
 * <div>
 * Verifica la existencia y el contenido de {@code webcontent/js/scene/MainMenuScene.js}, su
 * integración como segunda escena en la configuración del juego, el paso de la escena de arranque
 * al menú principal y el servicio del recurso a través del {@link GameServer}.
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
class MainMenuSceneTest
{
    /** Ruta del fichero de la escena del menú principal. */
    private static final Path MAIN_MENU_JS = Path.of("webcontent/js/scene/MainMenuScene.js");

    /** Ruta del fichero de la clase del botón de UI. */
    private static final Path UI_BUTTON_JS = Path.of("webcontent/js/ui/UIButton.js");

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
     * El fichero de la escena del menú principal existe.
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
    void mainMenuSceneFileExists()
    {
        assertTrue(Files.isRegularFile(MAIN_MENU_JS), "No existe webcontent/js/scene/MainMenuScene.js");
    }

    /**
     * <h2>
     * La escena extiende Phaser.Scene y declara el panel del menú.
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
    void mainMenuSceneExtendsPhaserSceneAndHasPanel() throws IOException
    {
        String scene = Files.readString(MAIN_MENU_JS);

        assertTrue(scene.contains("class MainMenuScene extends Phaser.Scene"), "La escena no extiende Phaser.Scene");
        assertTrue(scene.contains("MenuPanel001"), "Falta el panel del menú MenuPanel001");
        assertTrue(scene.contains("rgba(0.8, 0.8, 0.8, 0.75)"), "Falta el color de fondo del panel del menú");
        assertTrue(scene.contains("rgba(1.0, 1.0, 1.0, 1.0)"), "Falta el color del borde del panel del menú");
    }

    /**
     * <h2>
     * La escena declara las opciones del menú.
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
    void mainMenuSceneHasMenuButtons() throws IOException
    {
        String scene = Files.readString(MAIN_MENU_JS);

        assertTrue(scene.contains("MenuButtonNewGame"), "Falta el botón de nuevo juego");
        assertTrue(scene.contains("Nuevo juego"), "Falta el texto de nuevo juego");
        assertTrue(scene.contains("MenuButtonFullscreen"), "Falta el botón de pantalla completa");
        assertTrue(scene.contains("Alternar pantalla completa"), "Falta el texto de pantalla completa");
        assertTrue(scene.contains("MenuButtonCredits"), "Falta el botón de créditos");
        assertTrue(scene.contains("Créditos"), "Falta el texto de créditos");
        assertFalse(scene.contains("MenuButtonTestUI"), "El botón de prueba de UI no se ha eliminado");
        assertFalse(scene.contains("Test UI"), "El texto de prueba de UI no se ha eliminado");
        assertFalse(scene.contains("MenuButtonExit"), "El botón de salida no se ha eliminado");
        assertFalse(scene.contains("Salir del juego"), "El texto de salida no se ha eliminado");
        assertTrue(scene.contains("Monospace"), "Los botones no usan fuente Monospace");
    }

    /**
     * <h2>
     * El botón de nuevo juego oculta el menú y da paso a GameScene.
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
    void mainMenuNewGameButtonStartsGameScene() throws IOException
    {
        String scene = Files.readString(MAIN_MENU_JS);
        int newGameStart = scene.indexOf("id: 'MenuButtonNewGame'");
        int nextButtonStart = scene.indexOf("id: 'MenuButtonFullscreen'");

        assertTrue(newGameStart >= 0, "Falta el botón de nuevo juego");
        assertTrue(nextButtonStart > newGameStart, "No se puede delimitar el botón de nuevo juego");

        String newGameButton = scene.substring(newGameStart, nextButtonStart);
        assertTrue(newGameButton.contains("menuPanel.setVisible(false)"),
                "El botón de nuevo juego no oculta el panel del menú");
        assertTrue(newGameButton.contains("scene.start('GameScene')"),
                "El botón de nuevo juego no da paso a GameScene");
    }

    /**
     * <h2>
     * El botón de pantalla completa alterna entre ambos modos.
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
    void mainMenuFullscreenButtonTogglesFullscreenMode() throws IOException
    {
        String scene = Files.readString(MAIN_MENU_JS);

        assertTrue(scene.contains("this.scale.isFullscreen"), "No se consulta el estado de pantalla completa");
        assertTrue(scene.contains("this.scale.startFullscreen()"), "No se activa la pantalla completa");
        assertTrue(scene.contains("this.scale.stopFullscreen()"), "No se desactiva la pantalla completa");
    }

    /**
     * <h2>
     * Las opciones de prueba de UI y salida ya no están disponibles.
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
    void mainMenuDoesNotExposeTestOrExitActions() throws IOException
    {
        String scene = Files.readString(MAIN_MENU_JS);

        assertFalse(scene.contains("scene.start('UITestScene')"), "El menú todavía abre UITestScene");
        assertFalse(scene.contains("game.destroy"), "El menú todavía finaliza el juego");
    }

    /**
     * <h2>
     * La escena de arranque apunta a la introducción.
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
    void bootloaderTargetsIntroScene() throws IOException
    {
        String scene = Files.readString(BOOTLOADER_JS);

        assertTrue(scene.contains("nextScene"), "Falta la variable de la siguiente escena");
        assertTrue(scene.contains("'IntroScene'"), "La escena de arranque no apunta a IntroScene");
    }

    /**
     * <h2>
     * La configuración registra la introducción entre el arranque y el menú principal.
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
    void indexJsRegistersIntroBeforeMainMenu() throws IOException
    {
        String js = Files.readString(INDEX_JS);

        assertTrue(js.contains("BootloaderScene"), "La escena de arranque no está en la configuración");
        assertTrue(js.contains("IntroScene"), "La escena de introducción no está en la configuración");
        assertTrue(js.contains("MainMenuScene"), "La escena del menú principal no está en la configuración");
        assertTrue(js.contains("UITestScene"), "La escena de prueba de UI no está en la configuración");
        assertTrue(js.indexOf("IntroScene") > js.indexOf("BootloaderScene"),
                "La escena de introducción no está tras la de arranque");
        assertTrue(js.indexOf("MainMenuScene") > js.indexOf("IntroScene"),
                "La escena del menú principal no está tras la introducción");
        assertTrue(js.indexOf("UITestScene") > js.indexOf("MainMenuScene"),
                "UITestScene no está tras el menú principal");
    }

    /**
     * <h2>
     * El HTML carga la escena del menú principal antes del script principal.
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
    void indexHtmlLoadsMainMenuSceneBeforeIndexJs() throws IOException
    {
        String html = Files.readString(INDEX_HTML);

        int bootloaderIndex = html.indexOf("../js/scene/BootloaderScene.js");
        int introIndex = html.indexOf("../js/scene/IntroScene.js");
        int mainMenuIndex = html.indexOf("../js/scene/MainMenuScene.js");
        int uiTestSceneIndex = html.indexOf("../js/scene/UITestScene.js");
        int indexJsIndex = html.indexOf("../js/index.js");

        assertTrue(bootloaderIndex >= 0, "El HTML no carga la escena de arranque");
        assertTrue(introIndex >= 0, "El HTML no carga la escena de introducción");
        assertTrue(mainMenuIndex >= 0, "El HTML no carga la escena del menú principal");
        assertTrue(uiTestSceneIndex >= 0, "El HTML no carga la escena de prueba de UI");
        assertTrue(indexJsIndex >= 0, "El HTML no carga el script principal");
        assertTrue(mainMenuIndex < indexJsIndex, "MainMenuScene.js debe cargarse antes que index.js");
        assertTrue(bootloaderIndex < introIndex, "BootloaderScene.js debe cargarse antes que IntroScene.js");
        assertTrue(introIndex < mainMenuIndex, "IntroScene.js debe cargarse antes que MainMenuScene.js");
        assertTrue(mainMenuIndex < uiTestSceneIndex, "MainMenuScene.js debe cargarse antes que UITestScene.js");
    }

    /**
     * <h2>
     * El botón de UI soporta la ausencia de función de clic.
     * </h2>
     *
     * <div>
     * Los botones del menú sin {@code onClickFunction} (nuevo juego y créditos) no deben lanzar
     * error al hacer clic. El botón de UI únicamente invoca la función si está definida.
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
    void uiButtonHandlesMissingOnClickFunction() throws IOException
    {
        String uiButton = Files.readString(UI_BUTTON_JS);

        assertTrue(uiButton.contains("if (this.onClickFunction) {"),
                "El botón de UI no soporta la ausencia de onClickFunction");
    }

    /**
     * <h2>
     * El botón de créditos oculta el panel del menú y muestra los créditos.
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
    void mainMenuCreditsButtonHidesPanelAndShowsCredits() throws IOException
    {
        String scene = Files.readString(MAIN_MENU_JS);

        assertTrue(scene.contains("showCredits"), "El botón de créditos no inicia los créditos");
        assertTrue(scene.contains("menuPanel.setVisible(false)"), "Los créditos no ocultan el panel del menú");
    }

    /**
     * <h2>
     * La escena declara las secciones de los créditos.
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
    void mainMenuCreditsDeclareSections() throws IOException
    {
        String scene = Files.readString(MAIN_MENU_JS);

        assertTrue(scene.contains("CAST"), "Falta la sección CAST");
        assertTrue(scene.contains("Frontend Programmers"), "Falta la sección Frontend Programmers");
        assertTrue(scene.contains("A free AI agent specialized in programming"),
                "Falta la acreditación de Frontend Programmers");
        assertTrue(scene.contains("Backend Programmers"), "Falta la sección Backend Programmers");
        assertTrue(scene.contains("The same AI programming agent"),
                "Falta la acreditación de Backend Programmers");
        assertTrue(scene.contains("Assets Designers"), "Falta la sección Assets Designers");
        assertTrue(scene.contains("Two very famous general-purpose agents"),
                "Falta la acreditación de Assets Designers");
        assertTrue(scene.contains("Q&A Team"), "Falta la sección Q&A Team");
        assertTrue(scene.contains("The AI agent that actively participated in the development"),
                "Falta la acreditación de Q&A Team");
        assertTrue(scene.contains("... and the last but not least... in fact, the most important thing of all..."),
                "Falta la transición hacia los créditos finales");
        assertTrue(scene.contains("Concept, design and leading"), "Falta la sección Concept, design and leading");
        assertTrue(scene.contains("Me :)"), "Falta la acreditación Me :)");
    }

    /**
     * <h2>
     * Las secciones principales de los créditos recorren la pantalla en quince segundos.
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
    void mainMenuCreditsMainScrollsInFifteenSeconds() throws IOException
    {
        String scene = Files.readString(MAIN_MENU_JS);

        assertTrue(scene.contains("MAIN_MENU_CREDITS_MAIN_DURATION = 15000"),
                "La duración de los créditos principales no es de quince segundos");
        assertTrue(scene.contains("duration: MAIN_MENU_CREDITS_MAIN_DURATION"),
                "Los créditos principales no usan su duración declarada");
    }

    /**
     * <h2>
     * La sección final de los créditos usa doble tamaño de fuente y cinco segundos.
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
    void mainMenuCreditsConceptUsesDoubleFontAndFiveSeconds() throws IOException
    {
        String scene = Files.readString(MAIN_MENU_JS);

        assertTrue(scene.contains("MAIN_MENU_CREDITS_CONCEPT_FONT_MULTIPLIER = 2"),
                "La sección final de los créditos no declara el doble de tamaño de fuente");
        assertTrue(scene.contains("* MAIN_MENU_CREDITS_CONCEPT_FONT_MULTIPLIER"),
                "La sección final de los créditos no usa el doble de tamaño de fuente");
        assertTrue(scene.contains("MAIN_MENU_CREDITS_CONCEPT_DURATION = 5000"),
                "La duración de los créditos finales no es de cinco segundos");
        assertTrue(scene.contains("duration: MAIN_MENU_CREDITS_CONCEPT_DURATION"),
                "Los créditos finales no usan su duración declarada");
    }

    /**
     * <h2>
     * Al finalizar los créditos se vuelve a mostrar el panel del menú.
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
    void mainMenuCreditsRestoresPanelWhenFinished() throws IOException
    {
        String scene = Files.readString(MAIN_MENU_JS);

        assertTrue(scene.contains("menuPanel.setVisible(true)"),
                "Al finalizar los créditos no se vuelve a mostrar el panel del menú");
    }

    /**
     * <h2>
     * La escena del menú principal es servida por el servidor.
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
    void mainMenuResourcesAreServed() throws IOException, InterruptedException
    {
        HttpResponse<String> scene = sendGet("/js/scene/MainMenuScene.js");
        assertEquals(200, scene.statusCode());
        assertTrue(scene.headers().firstValue("Content-Type").orElse("").startsWith("application/javascript"));
        assertTrue(scene.body().contains("class MainMenuScene"));
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
