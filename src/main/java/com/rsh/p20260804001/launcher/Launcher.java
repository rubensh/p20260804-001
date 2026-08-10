package com.rsh.p20260804001.launcher;

import com.rsh.p20260804001.server.GameServer;

import java.io.IOException;

/**
 * <h1>
 * Launcher de la aplicación que arranca el servidor del juego.
 * </h1>
 *
 * <div>
 * Instancia un {@link GameServer} en el puerto por defecto 8088, lo pone a la escucha en
 * segundo plano, muestra el estado del servidor y mantiene viva la aplicación hasta que se
 * detenga. Registra un gancho de apagado que detiene el servidor y libera los recursos al
 * finalizar la ejecución vía CTRL-C o al cerrarse el terminal.
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
public class Launcher
{
    /** Puerto por defecto en el que escucha el servidor del juego. */
    private static final int DEFAULT_PORT = 8088;

    /** Servidor HTTP del juego gestionado por el launcher. */
    private final GameServer server;

    /**
     * <h2>
     * Constructor de la clase Launcher.
     * </h2>
     *
     * <div>
     * Crea un {@link GameServer} que atiende peticiones por el puerto por defecto 8088.
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
    public Launcher()
    {
        this(DEFAULT_PORT);
    }

    /**
     * <h2>
     * Constructor con puerto de escucha configurable.
     * </h2>
     *
     * <div>
     * Permite indicar un puerto distinto del predeterminado. Se emplea en las pruebas de la
     * clase.
     * </div>
     *
     * @param port
     *  Un {@link Integer} número de puerto por el que escuchará el servidor del juego.
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
    Launcher(int port)
    {
        this.server = new GameServer(port);
    }

    /**
     * <h2>
     * Método principal de la aplicación.
     * </h2>
     *
     * <div>
     * Crea un {@link Launcher} y arranca el servidor del juego en segundo plano.
     * </div>
     *
     * @param args
     *  Un array de {@link String} argumentos de línea de comandos (no empleados).
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
    public static void main(String[] args)
    {
        if (args == null || args.length == 0)
        {
            new Launcher().run();
        }
        else
        {
            try
            {
                new Launcher(Integer.parseInt(args[0])).run();
            }
            catch (Exception exception)
            {
                new Launcher().run();
            }
        }
    }

    /**
     * <h2>
     * Arranca el servidor del juego en segundo plano.
     * </h2>
     *
     * <div>
     * Inicializa el servidor, instala el gancho de apagado, muestra el mensaje de arranque y
     * mantiene el hilo en espera mientras el servidor esté en escucha.
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
    void run()
    {
        try
        {
            server.init(server.getPort());
            installShutdownHook();
            printStartupMessage();
            await();
        }
        catch (IOException e)
        {
            System.err.println("[GameServer] No se pudo iniciar el servidor: " + e.getMessage());
        }
    }

    /**
     * <h2>
     * Detiene el servidor del juego y libera los recursos adquiridos.
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
    public void stop()
    {
        server.stop();
    }

    /**
     * <h2>
     * Devuelve el puerto real de escucha del servidor del juego.
     * </h2>
     *
     * @return
     *  Un {@link Integer} número de puerto por el que escucha el servidor.
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
    public int getPort()
    {
        return server.getPort();
    }

    /**
     * <h2>
     * Instala el gancho de apagado del servidor.
     * </h2>
     *
     * <div>
     * Registra un hilo que detiene el servidor durante el apagado de la JVM, de modo que la
     * ejecución finaliza correctamente vía CTRL-C o al cerrarse el terminal.
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
    private void installShutdownHook()
    {
        // hilo anónimo que detiene el servidor durante el apagado de la JVM
        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.stop(), "game-server-shutdown"));
    }

    /**
     * <h2>
     * Muestra el mensaje de arranque y el estado del servidor.
     * </h2>
     *
     * <div>
     * Informa del inicio de la aplicación y del puerto de escucha real del servidor.
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
    private void printStartupMessage()
    {
        System.out.println("[GameServer] Arrancando el servidor de \"Buried Dark World\"...");
        System.out.println("[GameServer] Escuchando en http://localhost:" + server.getPort());
        System.out.println("[GameServer] Pulsa CTRL-C para detener el servidor.");
    }

    /**
     * <h2>
     * Mantiene el hilo de ejecución en espera mientras el servidor esté en escucha.
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
    private void await()
    {
        while (server.isRunning())
        {
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
