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
import static org.junit.jupiter.api.Assertions.assertFalse;

/** Verifica la retirada de la antigua landing page. */
class LandingPageTest
{
    private static final Path LANDING_HTML = Path.of("webcontent/pages/landing.html");
    private static final Path INDEX_HTML = Path.of("webcontent/pages/index.html");

    private HttpClient client;
    private int port;
    private GameServer server;

    @BeforeEach
    void setUp() throws IOException
    {
        server = new GameServer(0);
        server.init(0);
        port = server.getPort();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown()
    {
        server.stop();
    }

    @Test
    void landingPageIsRemoved()
    {
        assertFalse(Files.exists(LANDING_HTML), "landing.html debe estar eliminado");
    }

    @Test
    void indexDoesNotLinkToLandingPage() throws IOException
    {
        String html = Files.readString(INDEX_HTML);

        assertFalse(html.contains("landing.html"), "index.html todavía enlaza a landing.html");
    }

    @Test
    void landingPageIsNotServed() throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/pages/landing.html"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}
