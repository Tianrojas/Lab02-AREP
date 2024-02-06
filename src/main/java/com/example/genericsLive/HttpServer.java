package com.example.genericsLive;

import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.file.*;

/**
 * Clase que representa un servidor HTTP básico que maneja solicitudes GET para archivos locales.
 */
public class HttpServer {

    /**
     * Método principal que inicia el servidor HTTP.
     * @param args Argumentos de línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(35000)) {
            System.out.println("Servidor listo para recibir conexiones...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleRequest(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }

    /**
     * Maneja una solicitud HTTP recibida desde un cliente.
     * @param clientSocket El socket del cliente que realizó la solicitud.
     */
    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream outputStream = clientSocket.getOutputStream()) {

            String uriStr = "";
            String inputLine;
            boolean firstLine = true;
            String method = "";

            while ((inputLine = in.readLine()) != null) {
                if (firstLine) {
                    String[] requestParts = inputLine.split(" ");
                    uriStr = requestParts[1];
                    method = requestParts[0];
                    firstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }

            URI requestUri = new URI(uriStr);
            String response = htttpResponse(requestUri, method);
            outputStream.write(response.getBytes(StandardCharsets.ISO_8859_1));

        } catch (IOException | URISyntaxException e) {
            System.err.println("Error al manejar la solicitud: " + e.getMessage());
        }
    }

    /**
     * Genera la respuesta HTTP adecuada para una solicitud específica.
     * @param requestedURI La URI solicitada por el cliente.
     * @param method El método HTTP utilizado en la solicitud.
     * @return La respuesta HTTP generada.
     */
    private static String htttpResponse(URI requestedURI, String method) {
        if (requestedURI.getPath().equals("/movie")){
            return obtainHtmlRequest(method, requestedURI);
        } try {
            Path file = Paths.get("target/classes/public" + requestedURI.getPath());
            if (Files.exists(file) && !Files.isDirectory(file)) {
                byte[] fileContent = Files.readAllBytes(file);
                String contentType = getContentType(file.getFileName().toString());
                return createHttpResponse(200, contentType, fileContent);
            } else {
                return createHttpResponse(404, "text/html", httpError().getBytes());
            }
        } catch (IOException e) {
            System.err.println("Error al obtener el recurso: " + e.getMessage());
            return createHttpResponse(500, "text/plain", "Internal Server Error".getBytes());
        }
    }

    /**
     * Obtiene la respuesta HTML para la solicitud de la película.
     * @param method El método HTTP utilizado en la solicitud.
     * @param uri La URI solicitada por el cliente.
     * @return La respuesta HTML generada.
     */
    private static String obtainHtmlRequest(String method, URI uri) {
        JSONObject jsonResponse = HttpRequestHandler.handleRequest(method, uri);

        String title = jsonResponse.optString("Title", "");
        String year = jsonResponse.optString("Year", "");
        String rated = jsonResponse.optString("Rated", "");
        String released = jsonResponse.optString("Released", "");
        String runtime = jsonResponse.optString("Runtime", "");
        String genre = jsonResponse.optString("Genre", "");
        String director = jsonResponse.optString("Director", "");
        String plot = jsonResponse.optString("Plot", "");
        String imdbRating = jsonResponse.optString("imdbRating", "");
        String poster = jsonResponse.optString("Poster", "");

        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "<head>\r\n"
                + "<title>Movies</title>\r\n"
                + "</head>\r\n"
                + "<body>\r\n"
                + "<h1>" + title + " (" + year + ")</h1>\r\n"
                + "<div class=\"movie-details\">\r\n"
                + "<img src=\"" + poster + "\" alt=\"" + title + "\"> <br>\r\n"
                + "<strong>Rated:</strong> " + rated + "<br>\r\n"
                + "<strong>Released:</strong> " + released + "<br>\r\n"
                + "<strong>Runtime:</strong> " + runtime + "<br>\r\n"
                + "<strong>Genre:</strong> " + genre + "<br>\r\n"
                + "<strong>Director:</strong> " + director + "<br>\r\n"
                + "<strong>IMDb Rating:</strong> " + imdbRating + "<br>\r\n"
                + "<strong>Plot:</strong><br>\r\n"
                + "<p>" + plot + "</p>\r\n"
                + "</div>\r\n"
                + "</body>\r\n"
                + "</html>";
        return outputLine;
    }

    /**
     * Genera una página de error HTTP básica.
     * @return La página de error generada.
     */
    private static String httpError() {
        String errorPage = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Error Not found</title>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <h1>Error</h1>\n" +
                "    </body>\n" +
                "</html>";
        return errorPage;
    }

    /**
     * Crea una respuesta HTTP con el código de estado, tipo de contenido y contenido especificados.
     * @param statusCode El código de estado HTTP.
     * @param contentType El tipo de contenido HTTP.
     * @param content El contenido de la respuesta.
     * @return La respuesta HTTP generada.
     */
    private static String createHttpResponse(int statusCode, String contentType, byte[] content) {
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 ").append(statusCode).append(" OK\r\n");
        response.append("Content-Type: ").append(contentType).append("\r\n");
        response.append("Content-Length: ").append(content.length).append("\r\n");
        response.append("\r\n");
        response.append(new String(content, StandardCharsets.ISO_8859_1));
        return response.toString();
    }

    /**
     * Obtiene el tipo de contenido MIME basado en la extensión del archivo.
     * @param fileName El nombre del archivo.
     * @return El tipo de contenido MIME.
     */
    private static String getContentType(String fileName) {
        switch (getFileExtension(fileName)) {
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "js":
                return "application/javascript";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            default:
                return "application/octet-stream";
        }
    }

    /**
     * Obtiene la extensión de un archivo a partir de su nombre.
     * @param fileName El nombre del archivo.
     * @return La extensión del archivo.
     */
    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dotIndex + 1);
    }
}
