package com.example.genericsLive;

import org.json.JSONObject;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.*;

public class HttpRequestHandlerTest {

    @Test
    public void testHandleRequestGETSuccess() {
        // Prueba para verificar que la solicitud GET se maneje correctamente y se devuelva un objeto JSON válido

        // Creamos una URI de ejemplo con un nombre de película
        URI uri = URI.create("/movie?t=Guardians%20of%20the%20Galaxy");

        // Manejamos la solicitud GET
        JSONObject jsonResponse = HttpRequestHandler.handleRequest("GET", uri);

        // Verificamos que se devuelva un objeto JSON válido
        assertNotNull(jsonResponse);
        assertTrue(jsonResponse.has("Title"));
        assertTrue(jsonResponse.has("Year"));
        assertTrue(jsonResponse.has("Poster"));
    }

    @Test
    public void testHandleRequestGETIOException() {
        // Prueba para verificar que se maneje correctamente una IOException en la solicitud GET

        // Creamos una URI de ejemplo inválida
        URI uri = URI.create("/movie?t=iashjfbjadbjfd");

        // Manejamos la solicitud GET
        JSONObject jsonResponse = HttpRequestHandler.handleRequest("GET", uri);

        // Verificamos que se devuelva un objeto JSON de error
        assertNotNull(jsonResponse);
        assertTrue(jsonResponse.has("Title"));
        assertEquals("Resource not found", jsonResponse.getString("Title"));
        assertTrue(jsonResponse.has("Year"));
        assertTrue(jsonResponse.has("Poster"));
    }
}

