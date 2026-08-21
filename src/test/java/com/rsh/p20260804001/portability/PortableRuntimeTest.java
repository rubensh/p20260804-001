package com.rsh.p20260804001.portability;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <h1>
 * Pruebas de portabilidad del entorno Java incluido.
 * </h1>
 *
 * <div>
 * Verifica que el runtime generado con jlink contiene el módulo requerido, puede ejecutar el
 * launcher del juego y es utilizado por los scripts de arranque del proyecto.
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
class PortableRuntimeTest
{
    /** Versión principal mínima de Java requerida por el proyecto. */
    private static final String REQUIRED_JAVA_VERSION_PREFIX = "21.";

    /** Módulo mínimo requerido por la aplicación. */
    private static final String REQUIRED_JAVA_MODULE = "java.base";

    /** Nombre del artefacto ejecutable del proyecto. */
    private static final String APPLICATION_JAR = "target/p20260804-001-0.0.1.jar";

    /** Clase principal del juego. */
    private static final String MAIN_CLASS = "com.rsh.p20260804001.launcher.Launcher";

    /** Tiempo máximo de espera para arrancar o finalizar un subproceso. */
    private static final long PROCESS_TIMEOUT_SECONDS = 10;

    /** Intervalo entre comprobaciones del arranque del launcher. */
    private static final long STARTUP_POLL_MILLIS = 100;

    /** Indica si las pruebas se ejecutan en Windows. */
    private static final boolean WINDOWS = System.getProperty("os.name")
            .toLowerCase(Locale.ROOT).contains("win");

    /** Ejecutable Java incluido para la plataforma actual. */
    private static final Path RUNTIME_JAVA = Path.of("runtime", "bin", WINDOWS ? "java.exe" : "java");

    /**
     * <h2>
     * El runtime incluido contiene exclusivamente el módulo Java requerido.
     * </h2>
     *
     * @throws Exception
     *  Una {@link Exception} si no se puede consultar el runtime incluido.
     */
    @Test
    void bundledRuntimeContainsOnlyRequiredModule() throws Exception
    {
        ProcessResult result = executeRuntimeCommand("--list-modules");
        List<String> modules = result.output().lines().filter(line -> !line.isBlank()).toList();

        assertEquals(0, result.exitCode(), result.output());
        assertEquals(1, modules.size(), result.output());
        assertTrue(modules.getFirst().startsWith(REQUIRED_JAVA_MODULE + "@"), result.output());
    }

    /**
     * <h2>
     * El runtime incluido cumple la versión Java requerida.
     * </h2>
     *
     * @throws Exception
     *  Una {@link Exception} si no se puede consultar la versión del runtime.
     */
    @Test
    void bundledRuntimeUsesRequiredJavaVersion() throws Exception
    {
        ProcessResult result = executeRuntimeCommand("--version");

        assertEquals(0, result.exitCode(), result.output());
        assertTrue(result.output().contains(" " + REQUIRED_JAVA_VERSION_PREFIX), result.output());
    }

    /**
     * <h2>
     * El runtime conserva los avisos legales de Java.
     * </h2>
     */
    @Test
    void bundledRuntimeIncludesJavaLegalNotices()
    {
        Path legalDirectory = Path.of("runtime", "legal", REQUIRED_JAVA_MODULE);

        assertTrue(Files.isRegularFile(legalDirectory.resolve("LICENSE")));
        assertTrue(Files.isRegularFile(legalDirectory.resolve("ADDITIONAL_LICENSE_INFO")));
        assertTrue(Files.isRegularFile(legalDirectory.resolve("ASSEMBLY_EXCEPTION")));
    }

    /**
     * <h2>
     * Los scripts de arranque utilizan el runtime incluido.
     * </h2>
     *
     * @throws IOException
     *  Una {@link IOException} si no se pueden leer los scripts.
     */
    @Test
    void launchScriptsUseBundledRuntime() throws IOException
    {
        String expectedCommand = "runtime/bin/java -jar " + APPLICATION_JAR;
        String linuxScript = normalizeScript(Files.readString(Path.of("run.sh"), StandardCharsets.UTF_8));
        String windowsScript = normalizeScript(Files.readString(Path.of("run.bat"), StandardCharsets.UTF_8));

        assertTrue(linuxScript.contains(expectedCommand), linuxScript);
        assertTrue(windowsScript.contains(expectedCommand), windowsScript);
        assertFalse(linuxScript.lines().anyMatch(line -> line.strip().startsWith("java ")), linuxScript);
        assertFalse(windowsScript.lines().anyMatch(line -> line.strip().startsWith("java ")), windowsScript);
        if (!WINDOWS)
        {
            assertTrue(Files.isExecutable(Path.of("run.sh")), "run.sh debe tener permiso de ejecución");
        }
    }

    /**
     * <h2>
     * El runtime incluido puede arrancar el launcher del juego.
     * </h2>
     *
     * @throws Exception
     *  Una {@link Exception} si falla el arranque o la finalización del subproceso.
     */
    @Test
    void bundledRuntimeStartsApplicationLauncher() throws Exception
    {
        Path outputFile = Files.createTempFile("portable-runtime-launcher", ".txt");
        Process process = new ProcessBuilder(
                RUNTIME_JAVA.toString(),
                "-cp",
                Path.of("target", "classes").toString(),
                MAIN_CLASS,
                "0")
                .redirectErrorStream(true)
                .redirectOutput(outputFile.toFile())
                .start();
        try
        {
            assertTrue(awaitStartupMessage(process, outputFile), readOutput(outputFile));
        }
        finally
        {
            process.destroy();
            if (!process.waitFor(PROCESS_TIMEOUT_SECONDS, TimeUnit.SECONDS))
            {
                process.destroyForcibly();
            }
            Files.deleteIfExists(outputFile);
        }
    }

    /** Ejecuta una orden del runtime incluido y devuelve su salida y código de finalización. */
    private ProcessResult executeRuntimeCommand(String argument) throws Exception
    {
        assertTrue(Files.isRegularFile(RUNTIME_JAVA), "No existe el ejecutable " + RUNTIME_JAVA);
        if (!WINDOWS)
        {
            assertTrue(Files.isExecutable(RUNTIME_JAVA), RUNTIME_JAVA + " no es ejecutable");
        }

        Process process = new ProcessBuilder(RUNTIME_JAVA.toString(), argument)
                .redirectErrorStream(true)
                .start();
        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertTrue(process.waitFor(PROCESS_TIMEOUT_SECONDS, TimeUnit.SECONDS),
                "El runtime no finalizó a tiempo");
        return new ProcessResult(process.exitValue(), output);
    }

    /** Espera hasta que el launcher confirma que el servidor está escuchando. */
    private boolean awaitStartupMessage(Process process, Path outputFile) throws Exception
    {
        long deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(PROCESS_TIMEOUT_SECONDS);
        while (System.currentTimeMillis() < deadline)
        {
            if (readOutput(outputFile).contains("Escuchando en http://localhost:"))
            {
                return true;
            }
            if (!process.isAlive())
            {
                return false;
            }
            Thread.sleep(STARTUP_POLL_MILLIS);
        }
        return false;
    }

    /** Lee la salida acumulada por un subproceso. */
    private String readOutput(Path outputFile) throws IOException
    {
        return Files.readString(outputFile, StandardCharsets.UTF_8);
    }

    /** Normaliza separadores y espacios de un script de arranque. */
    private String normalizeScript(String script)
    {
        return script.replace('\\', '/').replace("\r\n", "\n").strip();
    }

    /** Resultado inmutable de la ejecución de una orden. */
    private record ProcessResult(int exitCode, String output)
    {
    }
}
