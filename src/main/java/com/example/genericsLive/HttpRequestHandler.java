package com.example.genericsLive;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * Clase para manejar las solicitudes HTTP y generar respuestas basadas en el método y la URI recibidos.
 */
public class HttpRequestHandler {

    /**
     * Maneja la solicitud HTTP y genera una respuesta basada en el método y la URI recibidos.
     * @param method el método HTTP de la solicitud (GET, POST, etc.)
     * @param uri la URI de la solicitud
     * @return un objeto JSON que representa la respuesta generada
     */
    public static JSONObject handleRequest(String method, URI uri) {
        switch (method) {
            case "GET":
                try {
                    return handleGetRequest(uri);
                } catch (IOException e) {
                    return new JSONObject(Map.of("Title", "Resource not found","Year","Sorry","Poster","https://img.freepik.com/vector-premium/lindo-gato-triste-sentado-lluvia-nube-dibujos-animados-vector-icono-ilustracion-animal-naturaleza-icono-aislado_138676-5215.jpg?w=826"));
                }
            case "POST":
                return handlePostRequest(uri);
            // Agregar casos para otros verbos REST como PUT, DELETE, etc. según sea necesario
            default:
                return new JSONObject(Map.of("Title", "HTTP/1.1 405 Method Not Allowed"));
        }
    }

    /**
     * Maneja las solicitudes GET y genera una respuesta basada en la URI recibida.
     * @param uri la URI de la solicitud GET
     * @return un objeto JSON que representa la respuesta generada
     * @throws IOException si ocurre un error durante el procesamiento de la solicitud
     */
    private static JSONObject handleGetRequest(URI uri) throws IOException {
        String movieName = uri.getQuery().split("=")[1];
        String normalizedMovieName = normalizeMovieName(movieName);

        JSONObject movieJSN = Cache.get(normalizedMovieName);
        if (movieJSN != null) {
            System.out.println("------------------------------------cache used");
            return movieJSN;
        } else {
            System.out.println("------------------------------------resource used");
            HttpConnection.setMovieName(movieName);
            movieJSN = HttpConnection.getMovieJSN();
            Cache.put(normalizedMovieName, movieJSN);
            return movieJSN;
        }
    }

    /**
     * Normaliza el nombre de la película para su uso en la caché.
     * @param movieName el nombre de la película a normalizar
     * @return el nombre de la película normalizado
     */
    private static String normalizeMovieName(String movieName) {
        return movieName.toLowerCase().trim();
    }

    /**
     * Maneja las solicitudes POST y genera una respuesta basada en la URI recibida.
     * @param uri la URI de la solicitud POST
     * @return un objeto JSON que representa la respuesta generada
     */
    private static JSONObject handlePostRequest(URI uri) {
        // Implementar la lógica para manejar solicitudes POST
        return new JSONObject(Map.of("Title", "Not implemented yet","Year","Sorry","Poster","https://img.freepik.com/vector-premium/lindo-gato-triste-sentado-lluvia-nube-dibujos-animados-vector-icono-ilustracion-animal-naturaleza-icono-aislado_138676-5215.jpg?w=826"));
    }

    // Métodos para manejar otros verbos REST y funcionalidades adicionales
}
