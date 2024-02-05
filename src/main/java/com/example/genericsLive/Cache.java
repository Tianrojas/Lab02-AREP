package com.example.genericsLive;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para almacenar y recuperar objetos JSON en caché.
 */
public class Cache {
    private static final Map<String, JSONObject> cacheMap = new HashMap<>();

    /**
     * Almacena un objeto JSON en la caché asociado con la clave especificada.
     * @param key la clave con la que se asociará el objeto JSON en la caché
     * @param value el objeto JSON que se almacenará en la caché
     */
    public static synchronized void put(String key, JSONObject value) {
        cacheMap.put(key, value);
    }

    /**
     * Recupera el objeto JSON asociado con la clave especificada desde la caché.
     * @param key la clave asociada con el objeto JSON que se desea recuperar
     * @return el objeto JSON asociado con la clave especificada, o null si la clave no está presente en la caché
     */
    public static synchronized JSONObject get(String key) {
        return cacheMap.get(key);
    }

    /**
     * Verifica si la caché contiene una entrada asociada con la clave especificada.
     * @param key la clave cuya presencia en la caché se va a verificar
     * @return true si la caché contiene una entrada asociada con la clave especificada, false de lo contrario
     */
    public static synchronized boolean containsKey(String key) {
        return cacheMap.containsKey(key);
    }
}
