package com.rsh.p20260804001.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * <h1>
 * Servidor HTTP ligero encargado de servir los recursos del juego.
 * </h1>
 *
 * <div>
 * Atiende exclusivamente peticiones HTTP GET sobre los contenidos existentes bajo la carpeta
 * {@code webcontent/} y sus subcarpetas. Cualquier otro tipo de petición es descartado. La
 * escucha de peticiones se ejecuta en segundo plano mediante un hilo demonio.
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
public class GameServer
{
    /** Tiempo máximo de espera, en milisegundos, para la lectura de una petición entrante. */
    private static final int SOCKET_TIMEOUT_MILLIS = 5000;

    /** Carpeta raíz que contiene los recursos servibles del juego. */
    private final Path webContentRoot;

    /** Puerto por el que el servidor escucha peticiones. */
    private int port;

    /** Indica si el servidor está en modo escucha. */
    private volatile boolean running;

    /** Socket de escucha de peticiones HTTP entrantes. */
    private ServerSocket serverSocket;

    /** Hilo demonio encargado de aceptar las conexiones entrantes. */
    private Thread acceptThread;

    /**
     * <h2>
     * Constructor de la clase GameServer.
     * </h2>
     *
     * <div>
     * Establece el número de puerto por el que escuchar peticiones.
     * </div>
     *
     * @param port
     *  Un {@link Integer} número de puerto por el que escuchar peticiones.
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
    public GameServer(int port)
    {
        this(port, Path.of("webcontent"));
    }

    /**
     * <h2>
     * Constructor con carpeta raíz de recursos configurable.
     * </h2>
     *
     * <div>
     * Permite indicar una carpeta raíz distinta de la predeterminada {@code webcontent}. Se
     * emplea en las pruebas de la clase.
     * </div>
     *
     * @param port
     *  Un {@link Integer} número de puerto por el que escuchar peticiones.
     *
     * @param webContentRoot
     *  Una {@link Path} carpeta raíz desde la que se sirven los recursos.
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
    GameServer(int port, Path webContentRoot)
    {
        this.port = port;
        this.webContentRoot = webContentRoot.toAbsolutePath().normalize();
    }

    /**
     * <h2>
     * Pone al servidor a la escucha de peticiones.
     * </h2>
     *
     * <div>
     * Crea un {@link ServerSocket} vinculado al puerto indicado e inicia, en segundo plano y
     * mediante un hilo demonio, el bucle de aceptación de conexiones.
     * </div>
     *
     * @param port
     *  Un {@link Integer} número de puerto por el que escuchar peticiones.
     *
     * @throws IOException
     *  Un {@link IOException} si no es posible abrir el socket de escucha.
     *
     * @throws IllegalStateException
     *  Un {@link IllegalStateException} si el servidor ya está en modo escucha.
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
    public void init(int port) throws IOException
    {
        if (running)
        {
            throw new IllegalStateException("El servidor ya está en modo escucha.");
        }
        serverSocket = new ServerSocket(port);
        this.port = serverSocket.getLocalPort();
        running = true;
        // método de referencia que delega el bucle de aceptación en el hilo demonio
        acceptThread = new Thread(this::acceptLoop, "game-server-acceptor");
        acceptThread.setDaemon(true);
        acceptThread.start();
    }

    /**
     * <h2>
     * Finaliza el modo escucha del servidor.
     * </h2>
     *
     * <div>
     * Cierra el socket de escucha y espera a que el hilo demonio de aceptación finalice.
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
    public void stop()
    {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed())
        {
            try
            {
                serverSocket.close();
            }
            catch (IOException e)
            {
                // socket de escucha ya cerrado
            }
        }
        if (acceptThread != null)
        {
            try
            {
                acceptThread.join(SOCKET_TIMEOUT_MILLIS);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * <h2>
     * Indica si el servidor está en modo escucha.
     * </h2>
     *
     * @return
     *  Un {@link Boolean} que será {@code true} si el servidor está en modo escucha.
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
    public boolean isRunning()
    {
        return running;
    }

    /**
     * <h2>
     * Devuelve el puerto por el que escucha el servidor.
     * </h2>
     *
     * @return
     *  Un {@link Integer} número de puerto real de escucha, una vez inicializado.
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
        return port;
    }

    /**
     * <h2>
     * Bucle de aceptación de conexiones entrantes.
     * </h2>
     *
     * <div>
     * Acepta conexiones de forma secuencial mientras el servidor esté en modo escucha.
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
    private void acceptLoop()
    {
        while (running)
        {
            try
            {
                handleConnection(serverSocket.accept());
            }
            catch (SocketException e)
            {
                if (!running)
                {
                    break;
                }
            }
            catch (IOException e)
            {
                if (!running)
                {
                    break;
                }
            }
        }
    }

    /**
     * <h2>
     * Atiende una conexión entrante.
     * </h2>
     *
     * <div>
     * Aplica un tiempo máximo de lectura y delega la atención de la petición.
     * </div>
     *
     * @param socket
     *  Un {@link Socket} conexión entrante aceptada por el servidor.
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
    private void handleConnection(Socket socket)
    {
        try (Socket connection = socket)
        {
            socket.setSoTimeout(SOCKET_TIMEOUT_MILLIS);
            handleRequest(socket);
        }
        catch (IOException e)
        {
            // conexión cerrada o petición no válida: no se responde
        }
    }

    /**
     * <h2>
     * Atiende una petición HTTP.
     * </h2>
     *
     * <div>
     * Lee la línea de petición y las cabeceras. Si el método es GET se sirve el recurso
     * solicitado; cualquier otro método es descartado.
     * </div>
     *
     * @param socket
     *  Un {@link Socket} conexión sobre la que se recibe y responde la petición.
     *
     * @throws IOException
     *  Un {@link IOException} si falla la lectura o escritura sobre la conexión.
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
    private void handleRequest(Socket socket) throws IOException
    {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.ISO_8859_1));
        OutputStream output = socket.getOutputStream();

        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isBlank())
        {
            return;
        }
        StringTokenizer tokenizer = new StringTokenizer(requestLine);
        if (tokenizer.countTokens() < 2)
        {
            sendSimpleResponse(output, 400, "Bad Request", "Bad Request");
            return;
        }
        String method = tokenizer.nextToken();
        String target = tokenizer.nextToken();

        String header;
        while ((header = reader.readLine()) != null && !header.isEmpty())
        {
            // cabeceras HTTP descartadas: no son necesarias para atender la petición
        }

        if (!"GET".equalsIgnoreCase(method))
        {
            sendMethodNotAllowed(output);
            return;
        }
        serveResource(output, target);
    }

    /**
     * <h2>
     * Sirve el recurso solicitado.
     * </h2>
     *
     * <div>
     * Resuelve el recurso dentro de la carpeta raíz y, si existe, lo envía con su cabecera
     * correspondiente.
     * </div>
     *
     * @param output
     *  Un {@link OutputStream} destino de la respuesta HTTP.
     *
     * @param target
     *  Un {@link String} recurso solicitado en la petición.
     *
     * @throws IOException
     *  Un {@link IOException} si falla la escritura de la respuesta o la lectura del recurso.
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
    private void serveResource(OutputStream output, String target) throws IOException
    {
        Path resource = resolveResource(target);
        if (resource == null)
        {
            sendSimpleResponse(output, 403, "Forbidden", "Forbidden");
            return;
        }
        if (!Files.isRegularFile(resource) || !Files.isReadable(resource))
        {
            sendSimpleResponse(output, 404, "Not Found", "Not Found");
            return;
        }
        sendHeaders(output, 200, "OK", contentTypeFor(resource), Files.size(resource));
        Files.copy(resource, output);
        output.flush();
    }

    /**
     * <h2>
     * Resuelve la ruta de un recurso dentro de la carpeta raíz.
     * </h2>
     *
     * <div>
     * Decodifica la ruta solicitada y la normaliza. Devuelve {@code null} si la ruta resuelta
     * queda fuera de la carpeta raíz de contenidos.
     * </div>
     *
     * @param target
     *  Un {@link String} recurso solicitado en la petición.
     *
     * @return
     *  Una {@link Path} recurso normalizado dentro de la carpeta raíz, o {@code null}.
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
    private Path resolveResource(String target)
    {
        String pathPart = target;
        int queryIndex = pathPart.indexOf('?');
        if (queryIndex >= 0)
        {
            pathPart = pathPart.substring(0, queryIndex);
        }
        String decoded;
        try
        {
            decoded = URLDecoder.decode(pathPart, StandardCharsets.UTF_8);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
        if (decoded.startsWith("/"))
        {
            decoded = decoded.substring(1);
        }
        Path resource = webContentRoot.resolve(decoded).normalize();
        if (!resource.startsWith(webContentRoot))
        {
            return null;
        }
        return resource;
    }

    /**
     * <h2>
     * Envía una respuesta HTTP con cuerpo de texto plano.
     * </h2>
     *
     * @param output
     *  Un {@link OutputStream} destino de la respuesta HTTP.
     *
     * @param statusCode
     *  Un {@link Integer} código de estado HTTP de la respuesta.
     *
     * @param reason
     *  Un {@link String} descripción textual del estado HTTP.
     *
     * @param body
     *  Un {@link String} cuerpo de la respuesta.
     *
     * @throws IOException
     *  Un {@link IOException} si falla la escritura de la respuesta.
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
    private void sendSimpleResponse(OutputStream output, int statusCode, String reason, String body) throws IOException
    {
        byte[] data = body.getBytes(StandardCharsets.UTF_8);
        sendHeaders(output, statusCode, reason, "text/plain; charset=utf-8", data.length);
        output.write(data);
        output.flush();
    }

    /**
     * <h2>
     * Envía una respuesta HTTP 405 Method Not Allowed.
     * </h2>
     *
     * @param output
     *  Un {@link OutputStream} destino de la respuesta HTTP.
     *
     * @throws IOException
     *  Un {@link IOException} si falla la escritura de la respuesta.
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
    private void sendMethodNotAllowed(OutputStream output) throws IOException
    {
        byte[] body = "Method Not Allowed".getBytes(StandardCharsets.UTF_8);
        String headers = "HTTP/1.1 405 Method Not Allowed\r\n"
                + "Allow: GET\r\n"
                + "Content-Type: text/plain; charset=utf-8\r\n"
                + "Content-Length: " + body.length + "\r\n"
                + "Connection: close\r\n"
                + "\r\n";
        output.write(headers.getBytes(StandardCharsets.ISO_8859_1));
        output.write(body);
        output.flush();
    }

    /**
     * <h2>
     * Envía las cabeceras de una respuesta HTTP.
     * </h2>
     *
     * @param output
     *  Un {@link OutputStream} destino de la respuesta HTTP.
     *
     * @param statusCode
     *  Un {@link Integer} código de estado HTTP de la respuesta.
     *
     * @param reason
     *  Un {@link String} descripción textual del estado HTTP.
     *
     * @param contentType
     *  Un {@link String} tipo de contenido de la respuesta.
     *
     * @param contentLength
     *  Un {@link Long} longitud del contenido de la respuesta.
     *
     * @throws IOException
     *  Un {@link IOException} si falla la escritura de las cabeceras.
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
    private void sendHeaders(OutputStream output, int statusCode, String reason, String contentType, long contentLength)
            throws IOException
    {
        String headers = "HTTP/1.1 " + statusCode + " " + reason + "\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + contentLength + "\r\n"
                + "Connection: close\r\n"
                + "\r\n";
        output.write(headers.getBytes(StandardCharsets.ISO_8859_1));
    }

    /**
     * <h2>
     * Devuelve el tipo de contenido de un recurso según su extensión.
     * </h2>
     *
     * @param resource
     *  Una {@link Path} recurso del que se desea conocer el tipo de contenido.
     *
     * @return
     *  Un {@link String} tipo de contenido MIME del recurso.
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
    private static String contentTypeFor(Path resource)
    {
        String fileName = resource.getFileName().toString();
        int dot = fileName.lastIndexOf('.');
        String extension = dot < 0 ? "" : fileName.substring(dot + 1).toLowerCase(Locale.ROOT);
        return switch (extension)
        {
            case "html", "htm" -> "text/html; charset=utf-8";
            case "css" -> "text/css; charset=utf-8";
            case "js", "mjs" -> "application/javascript; charset=utf-8";
            case "json" -> "application/json; charset=utf-8";
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "gif" -> "image/gif";
            case "svg" -> "image/svg+xml";
            case "ico" -> "image/x-icon";
            case "woff" -> "font/woff";
            case "woff2" -> "font/woff2";
            case "txt" -> "text/plain; charset=utf-8";
            default -> "application/octet-stream";
        };
    }
}
