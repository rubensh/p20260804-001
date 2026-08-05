package com.rsh.p20260804001.launcher;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * <h1>
 * Pruebas de la clase Launcher.
 * </h1>
 *
 * <div>
 * Verifica el puerto por defecto, el arranque en segundo plano del servidor y la finalización
 * de la aplicación vía CTRL-C con liberación de los recursos adquiridos.
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
class LauncherTest
{
    /** Puerto por defecto en el que escucha el servidor del juego. */
    private static final int DEFAULT_PORT = 8088;

    /** Tiempo máximo, en milisegundos, de espera de arranque del servidor. */
    private static final long STARTUP_TIMEOUT_MILLIS = 10000;

    /** Cliente HTTP empleado para enviar las peticiones de prueba. */
    private final HttpClient client = HttpClient.newHttpClient();

    /**
     * <h2>
     * El launcher por defecto escucha por el puerto 8088.
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
    void defaultPortIs8088()
    {
        assertEquals(DEFAULT_PORT, new Launcher().getPort());
    }

    /**
     * <h2>
     * El launcher arranca el servidor en segundo plano y atiende peticiones GET.
     * </h2>
     *
     * <div>
     * Ejecuta el arranque en un hilo, verifica que el servidor responde y que el hilo de
     * ejecución finaliza al detener el servidor.
     * </div>
     *
     * @throws Exception
     *  Una {@link Exception} si falla la petición de prueba o la espera de arranque.
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
    void launcherStartsServerAndServesGetRequests() throws Exception
    {
        Launcher launcher = new Launcher(0);
        // hilo demonio que ejecuta el arranque del launcher
        Thread launcherThread = new Thread(() -> launcher.run(), "launcher-test");
        launcherThread.setDaemon(true);
        launcherThread.start();

        int port = awaitPort(launcher);
        assertTrue(port > 0, "El servidor no arrancó a tiempo");

        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/no-existe.html")).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());

        launcher.stop();
        launcherThread.join(5000);
        assertFalse(launcherThread.isAlive(), "El hilo del launcher no finalizó tras detener el servidor");
    }

    /**
     * <h2>
     * La aplicación finaliza correctamente al enviar CTRL-C.
     * </h2>
     *
     * <div>
     * Lanza el {@link Launcher} en un subproceso, espera a que el servidor responda en el
     * puerto 8088, envía una señal de terminación equivalente a CTRL-C y verifica que el
     * proceso finaliza liberando los recursos.
     * </div>
     *
     * @throws Exception
     *  Una {@link Exception} si falla la ejecución o la verificación del subproceso.
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
    void ctrlCTerminatesServerAndReleasesPort() throws Exception
    {
        Path outputFile = Files.createTempFile("launcher-output", ".txt");
        try
        {
            // subproceso Java que lanza el Launcher con el classpath de ejecución actual
            Process process = new ProcessBuilder("java", "-cp", System.getProperty("java.class.path"),
                    Launcher.class.getName())
                    .redirectErrorStream(true)
                    .redirectOutput(outputFile.toFile())
                    .start();
            try
            {
                awaitHttpGetOnDefaultPort();
                process.destroy();
                assertTrue(process.waitFor(10, TimeUnit.SECONDS), "El proceso no finalizó tras enviar CTRL-C");
                String output = Files.readString(outputFile, StandardCharsets.UTF_8);
                assertTrue(output.contains(String.valueOf(DEFAULT_PORT)), output);
            }
            finally
            {
                process.destroyForcibly();
            }
        }
        finally
        {
            Files.deleteIfExists(outputFile);
        }
    }

    /**
     * <h2>
     * Espera a que el servidor del launcher acepte conexiones.
     * </h2>
     *
     * @param launcher
     *  Un {@link Launcher} del que se consulta el puerto real de escucha.
     *
     * @return
     *  Un {@link Integer} puerto real de escucha, o {@code -1} si no arranca a tiempo.
     *
     * @throws InterruptedException
     *  Una {@link InterruptedException} si se interrumpe la espera de arranque.
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
    private int awaitPort(Launcher launcher) throws InterruptedException
    {
        long deadline = System.currentTimeMillis() + STARTUP_TIMEOUT_MILLIS;
        while (System.currentTimeMillis() < deadline)
        {
            int port = launcher.getPort();
            if (port > 0)
            {
                try (Socket ignored = new Socket("localhost", port))
                {
                    return port;
                }
                catch (IOException e)
                {
                    // servidor aún no listo para aceptar conexiones: reintenta
                }
            }
            Thread.sleep(100);
        }
        return -1;
    }

    /**
     * <h2>
     * Espera a que el servidor responda una petición GET en el puerto 8088.
     * </h2>
     *
     * @throws Exception
     *  Una {@link Exception} si falla la petición de prueba o se agota el tiempo de espera.
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
    private void awaitHttpGetOnDefaultPort() throws Exception
    {
        long deadline = System.currentTimeMillis() + STARTUP_TIMEOUT_MILLIS;
        while (System.currentTimeMillis() < deadline)
        {
            try
            {
                HttpResponse<String> response = client.send(
                        HttpRequest.newBuilder(URI.create("http://localhost:8088/no-existe.html")).GET().build(),
                        HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 404)
                {
                    return;
                }
            }
            catch (IOException e)
            {
                // servidor aún no disponible: reintenta
            }
            Thread.sleep(200);
        }
        fail("El servidor no respondió en el puerto 8088 a tiempo");
    }
}
