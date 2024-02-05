package com.example.genericsLive;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Clase para realizar conexiones HTTP y obtener información de películas utilizando la API de OMDB.
 */
public class HttpConnection {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "https://www.omdbapi.com/";
    private static final String API_KEY = "e687741e";
    private static String movieName;
    private static JSONObject movieJSN;

    /**
     * Realiza una solicitud HTTP GET para obtener información sobre la película especificada.
     * @throws IOException si ocurre un error durante la conexión o la lectura de la respuesta
     */
    public static void performRequest() throws JSONException, IOException {

        URL obj = new URL(buildURL(movieName));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            movieJSN = new JSONObject(response.toString());
        } else {
            System.out.println("GET request not worked");
        }
        System.out.println("GET DONE");
    }

    /**
     * Construye la URL para la solicitud GET a la API de OMDB con el nombre de la película especificado.
     * @param movieName el nombre de la película
     * @return la URL completa para la solicitud
     */
    public static String buildURL(String movieName){
        return GET_URL+"?apikey="+API_KEY+"&t="+movieName;
    }

    /**
     * Obtiene el objeto JSON que contiene la información de la película obtenida de la respuesta.
     * @return el objeto JSON con la información de la película
     */
    public static JSONObject getMovieJSN() {
        return movieJSN;
    }

    /**
     * Establece el nombre de la película para la cual se realizará la solicitud y realiza la solicitud.
     * @param movieName el nombre de la película
     * @throws IOException si ocurre un error durante la conexión o la lectura de la respuesta
     */
    public static void setMovieName(String movieName) throws IOException {
        HttpConnection.movieName = movieName;
        HttpConnection.performRequest();
        if ((movieJSN.optString("Title", "")).isEmpty()){
            throw new IOException();
        }
    }
}